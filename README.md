## Atomization of Form Ingestion with Logic APP

This code sample demonstrates how to implement a Logic APP integrate it with Form Recognizer V.2, and ingest forms from outlook into a Database. 

# Prerequisites.
1. Azure Subscription
2. Logic App 
3. Azure Synapse or (Databricks a & Azure Data Warehouse)
4. Data Lake or (Blob Storage)
5. Form Recognizer


  ## Setting Up the Logic App.
  
  1. Create a blank logic app 
    ![image](https://docs.microsoft.com/en-us/azure/logic-apps/media/quickstart-create-first-logic-app-workflow/choose-logic-app-template.png)

  2. In the search bar for triggers, search for office 365 Outlook.
 * Select When a new email arrive (v3) as shown in the image below
  ![When a new email ](https://user-images.githubusercontent.com/37972820/95350656-763f9e80-08d1-11eb-9d17-01383afdba7f.png)
3. Sign in using you o365 account.
    *  Select Yes from the drop down for  "Only with Attachments " & "Include attachments" 
    *  Add additional Parameters for 
        * Subject Line
        * From
![Enabling Additional Parameters](https://user-images.githubusercontent.com/37972820/95351592-6d9b9800-08d2-11eb-9ccb-7b8222316378.png)
4. Click on + New Step
5. search for form recognizer
![Screenshot 2020-10-07 193345](https://user-images.githubusercontent.com/37972820/95353178-08e13d00-08d4-11eb-91a9-9ce0e4c08a7e.png)

6. Select Train Model and fill up the information below
![Screenshot 2020-10-07 193209](https://user-images.githubusercontent.com/37972820/95353029-df281600-08d3-11eb-9cd6-c0b608e8c69c.png)
    * Connection Name (A given Name)
    * Site URL (Form Recognizer Endpoint from Azure)
    * Account Key (Form Recognizer Key from Azure)

__To get the infomation needed above.__ 
* Navigate to the form recognizer instance in azure 
* Select Keys and Endpoints 
    * copy the key and add it to account key
    * copy the end point and paste it into your Site URL
    *![Screenshot 2020-10-07 193609](https://user-images.githubusercontent.com/37972820/95353501-62496c00-08d4-11eb-8647-bbebef88dacc.png)

7. Insert the Source URL from Microsoft Azure Storage Explorer. 
    *   Find your Subscription in which the storage account is deployed in
    *   Navigate into your storage account 
    *   Select the blob that contians the traning data set
    *   Copy the URL from the Bottom Left Corner
    *   paste it as the source.
    ![blob storage url](https://user-images.githubusercontent.com/37972820/95357045-6d060000-08d8-11eb-97f1-e3bfe005ce08.png)
