# Configure the Azure provider
terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.89.0"
    }
  }

  required_version = ">= 1.1.0"
}

provider "azurerm" {
  features {}
}

### Create resource group
resource "azurerm_resource_group" "student-application-demo" {
  name     = "student-application-demo"
  location = "ukwest"
}

### Create service plan
resource "azurerm_service_plan" "student-application-demo" {
  name                = "student-application-demo-service-plan"
  location            = azurerm_resource_group.student-application-demo.location
  resource_group_name = azurerm_resource_group.student-application-demo.name
  os_type             = "Linux"
  sku_name            = "Y1"
}

### Create storage needed for functions
resource "azurerm_storage_account" "student-application-demo" {
  name                     = "studentapplicationdemo"
  resource_group_name      = azurerm_resource_group.student-application-demo.name
  location                 = azurerm_resource_group.student-application-demo.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

### Create azure event hub
resource "azurerm_eventhub_namespace" "student-application-demo" {
  name                = "student-application-demo"
  location            = azurerm_resource_group.student-application-demo.location
  resource_group_name = azurerm_resource_group.student-application-demo.name
  sku                 = "Standard"
  capacity            = 1
}
resource "azurerm_eventhub" "events" {
  name                = "events"
  namespace_name      = azurerm_eventhub_namespace.student-application-demo.name
  resource_group_name = azurerm_resource_group.student-application-demo.name
  partition_count     = 2
  message_retention   = 1
}
resource "azurerm_eventhub_consumer_group" "student-application-demo" {
  name                = "student-application-demo"
  namespace_name      = azurerm_eventhub_namespace.student-application-demo.name
  resource_group_name = azurerm_resource_group.student-application-demo.name
  eventhub_name       = azurerm_eventhub.events.name
}

### Create azure functions
resource "azurerm_linux_function_app" "application-function" {
  name                        = "application-function"
  location                    = azurerm_resource_group.student-application-demo.location
  resource_group_name         = azurerm_resource_group.student-application-demo.name
  service_plan_id             = azurerm_service_plan.student-application-demo.id
  storage_account_name        = azurerm_storage_account.student-application-demo.name
  storage_account_access_key  = azurerm_storage_account.student-application-demo.primary_access_key
  functions_extension_version = "~4"

  site_config {
    application_stack {
      java_version = "17"
    }
  }

  app_settings = {
    EVENT_HUBS_CONNECTION_STRING = azurerm_eventhub_namespace.student-application-demo.default_primary_connection_string
  }
}
resource "azurerm_linux_function_app" "student-function" {
  name                        = "student-function"
  location                    = azurerm_resource_group.student-application-demo.location
  resource_group_name         = azurerm_resource_group.student-application-demo.name
  service_plan_id             = azurerm_service_plan.student-application-demo.id
  storage_account_name        = azurerm_storage_account.student-application-demo.name
  storage_account_access_key  = azurerm_storage_account.student-application-demo.primary_access_key
  functions_extension_version = "~4"

  site_config {
    application_stack {
      java_version = "17"
    }
  }

  app_settings = {
    EVENT_HUBS_CONNECTION_STRING = azurerm_eventhub_namespace.student-application-demo.default_primary_connection_string
  }
}

### Create azure streaming
resource "azurerm_stream_analytics_job" "events-to-table-storage" {
  name                                     = "events-to-table-storage"
  resource_group_name                      = azurerm_resource_group.student-application-demo.name
  location                                 = azurerm_resource_group.student-application-demo.location
  compatibility_level                      = "1.2"
  data_locale                              = "en-GB"
  events_late_arrival_max_delay_in_seconds = 60
  events_out_of_order_max_delay_in_seconds = 50
  events_out_of_order_policy               = "Adjust"
  output_error_policy                      = "Drop"
  streaming_units                          = 3

  transformation_query = <<QUERY
    SELECT *
    INTO [storage-table-output]
    FROM [eventhub-events-input]
  QUERY
}
resource "azurerm_storage_table" "events" {
  name                 = "events"
  storage_account_name = azurerm_storage_account.student-application-demo.name
}
resource "azurerm_stream_analytics_stream_input_eventhub" "events-input" {
  name                         = "eventhub-events-input"
  stream_analytics_job_name    = azurerm_stream_analytics_job.events-to-table-storage.name
  resource_group_name          = azurerm_stream_analytics_job.events-to-table-storage.resource_group_name
  eventhub_consumer_group_name = azurerm_eventhub_consumer_group.student-application-demo.name
  eventhub_name                = azurerm_eventhub.events.name
  servicebus_namespace         = azurerm_eventhub_namespace.student-application-demo.name
  shared_access_policy_key     = azurerm_eventhub_namespace.student-application-demo.default_primary_key
  shared_access_policy_name    = "RootManageSharedAccessKey"

  serialization {
    type     = "Json"
    encoding = "UTF8"
  }
}
resource "azurerm_stream_analytics_output_table" "events-storage-table-output" {
  name                      = "storage-table-output"
  stream_analytics_job_name = azurerm_stream_analytics_job.events-to-table-storage.name
  resource_group_name       = azurerm_stream_analytics_job.events-to-table-storage.resource_group_name
  storage_account_name      = azurerm_storage_account.student-application-demo.name
  storage_account_key       = azurerm_storage_account.student-application-demo.primary_access_key
  table                     = azurerm_storage_table.events.name
  partition_key             = "id"
  row_key                   = "id"
  batch_size                = 100
}
resource "azurerm_stream_analytics_job" "register-student-upon-application-approval" {
  name                                     = "register-student-upon-application-approval"
  resource_group_name                      = azurerm_resource_group.student-application-demo.name
  location                                 = azurerm_resource_group.student-application-demo.location
  compatibility_level                      = "1.2"
  data_locale                              = "en-GB"
  events_late_arrival_max_delay_in_seconds = 60
  events_out_of_order_max_delay_in_seconds = 50
  events_out_of_order_policy               = "Adjust"
  output_error_policy                      = "Drop"
  streaming_units                          = 3

  transformation_query = <<QUERY
    SELECT
        payload.firstname AS firstname,
        payload.surname AS surname,
        payload.courseId AS courseId
    INTO [function-register-student-output]
    FROM [eventhub-events-approval-input]
    WHERE type = 'ApplicationApproved'
  QUERY
}
resource "azurerm_stream_analytics_stream_input_eventhub" "events-approval-input" {
  name                         = "eventhub-events-approval-input"
  stream_analytics_job_name    = azurerm_stream_analytics_job.register-student-upon-application-approval.name
  resource_group_name          = azurerm_stream_analytics_job.register-student-upon-application-approval.resource_group_name
  eventhub_consumer_group_name = azurerm_eventhub_consumer_group.student-application-demo.name
  eventhub_name                = azurerm_eventhub.events.name
  servicebus_namespace         = azurerm_eventhub_namespace.student-application-demo.name
  shared_access_policy_key     = azurerm_eventhub_namespace.student-application-demo.default_primary_key
  shared_access_policy_name    = "RootManageSharedAccessKey"

  serialization {
    type     = "Json"
    encoding = "UTF8"
  }
}
resource "azurerm_stream_analytics_output_function" "function-register-student-output" {
  name                      = "function-register-student-output"
  resource_group_name       = azurerm_resource_group.student-application-demo.name
  stream_analytics_job_name = azurerm_stream_analytics_job.register-student-upon-application-approval.name
  function_app              = azurerm_linux_function_app.student-function.name
  function_name             = "register-student-bulk"
  api_key                   = azurerm_linux_function_app.student-function.storage_account_access_key
}