## Data-AI Form parsing to Insights Demo
![image](https://user-images.githubusercontent.com/11376115/98652982-da80d280-2355-11eb-897a-16718c681e52.png)


## Automation of Form Ingestion with Logic APP

This code sample demonstrates how to implement a Logic APP integrate it with Form Recognizer V.2, and ingest forms from outlook into a Database. 

# Prerequisites.
1. Azure Subscription
2. Logic App 
3. Azure Synapse or (Databricks a & Azure Data Warehouse)
4. Data Lake or (Blob Storage)
5. Form Recognizer


  

  ### Setting Up the Logic App.
  
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
5. Search for form recognizer
![Screenshot 2020-10-07 193345](https://user-images.githubusercontent.com/37972820/95353178-08e13d00-08d4-11eb-91a9-9ce0e4c08a7e.png)

6. Select Train Model and fill up the information below
![Screenshot 2020-10-07 193209](https://user-images.githubusercontent.com/37972820/95353029-df281600-08d3-11eb-9cd6-c0b608e8c69c.png)
    * Connection Name (A given Name)
    * Site URL (Form Recognizer Endpoint from Azure)
    * Account Key (Form Recognizer Key from Azure)

__To get the infomation needed above.__ 
* Navigate to the form recognizer instance in azure 
* Select Keys and Endpoints 
    * Copy the key and add it to account key
    * Copy the end point and paste it into your Site URL
    ![Screenshot 2020-10-07 193609](https://user-images.githubusercontent.com/37972820/95353501-62496c00-08d4-11eb-8647-bbebef88dacc.png)

7. Insert the Source URL from Microsoft Azure Storage Explorer. 
    *   Find your Subscription in which the storage account is deployed in
    *   Navigate into your storage account 
    *   Select the blob that contains the training data set
    *   Copy the URL from the Bottom Left Corner
    *   Paste it as the source
    ![blob storage url](https://user-images.githubusercontent.com/37972820/95357045-6d060000-08d8-11eb-97f1-e3bfe005ce08.png)

8. Click on + Next Step
9. Search for Form Recognizer and Select Analyse Form
    * Click on Model ID and Select __ModelID__ that is generated from __Train Model__
    * Click on Document and Select __Attachment Content__ from __When an Email is Received__
    ![Screenshot 2020-10-07 202624](https://user-images.githubusercontent.com/37972820/95359619-6dec6100-08db-11eb-86be-22a5a1b47d4b.png)
10. Add an Action __Data Operations__
    * Select Compose 
    * Tables in the input Bar
11. Test it out.

  ### Processing datalake data using databricks
  1. First you will need to create databricks in your azure portal
  2. Create a spark cluster from databricks
  3. Import the sample scala notebook for processing form recognizer data "ProcessFormRecognizer Code"
  4. Example of the notebook code
  ![image](https://user-images.githubusercontent.com/11376115/98465422-65d95700-21e2-11eb-85e4-994786244826.png)
  5. For incremental process, you will need to be careful to add a condition at cmd11 to change instead of creating the table, to insert instead. This will defintely require a condition to be created first to validate if it is a first time run, or an incremental batch
  6. Moving forward you will push the data into the Synapse SQL Pool (first you will need to have the Synapse provisioned and available)
  7. Example of pushing the data to Synapse below
  ![image](https://user-images.githubusercontent.com/11376115/98465764-ad60e280-21e4-11eb-8f47-d78c1c7fb17a.png)
  8. Last command below, takes care of moving processed files into an archive directory under data lake, sample below
  ![image](https://user-images.githubusercontent.com/11376115/98465771-c36ea300-21e4-11eb-8f6d-cb88fe0d2134.png)
    
