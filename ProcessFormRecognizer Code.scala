// Databricks notebook source
val containerName = ""
val storageAccountName = ""
val sas = ""
val config = "fs.azure.sas." + containerName+ "." + storageAccountName + ".blob.core.windows.net"

// COMMAND ----------

spark.conf.set(
  "fs.azure.account.key.<StorageName>.dfs.core.windows.net",
  "AccountKey"
)

// COMMAND ----------

val df = spark.read.option("multiline",true).json("abfss://<ContainerName>@<StorageName>.dfs.core.windows.net/data.json")

// COMMAND ----------

display(df)

// COMMAND ----------

// MAGIC %sql
// MAGIC drop table dummytable

// COMMAND ----------

df.write.saveAsTable("dummytable")

// COMMAND ----------

// MAGIC %sql
// MAGIC drop table temptbl1

// COMMAND ----------

// MAGIC %sql
// MAGIC Select ReadResults.Lines[0] from dummytable

// COMMAND ----------

// MAGIC %sql
// MAGIC create table temptbl1 as
// MAGIC Select PageResults.Tables[0].Cells[0] as entries2 from dummytable

// COMMAND ----------

// MAGIC %sql
// MAGIC Select entries2[4].Text Description, entries2[5].Text Quatity, entries2[6].Text UnitPrice, entries2[7].Text amount  from temptbl1
// MAGIC Union Select entries2[8].Text Description, entries2[9].Text Quatity, entries2[10].Text UnitPrice, entries2[11].Text amount  from temptbl1
// MAGIC Union Select entries2[12].Text Description, entries2[13].Text Quatity, entries2[14].Text UnitPrice, entries2[15].Text amount  from temptbl1
// MAGIC Union Select entries2[16].Text Description, entries2[17].Text Quatity, entries2[18].Text UnitPrice, entries2[19].Text amount  from temptbl1

// COMMAND ----------

// MAGIC %sql
// MAGIC create table dailyOrders as 
// MAGIC Select entries2[4].Text Description, entries2[5].Text Quatity, entries2[6].Text UnitPrice, entries2[7].Text amount  from temptbl1
// MAGIC Union Select entries2[8].Text Description, entries2[9].Text Quatity, entries2[10].Text UnitPrice, entries2[11].Text amount  from temptbl1
// MAGIC Union Select entries2[12].Text Description, entries2[13].Text Quatity, entries2[14].Text UnitPrice, entries2[15].Text amount  from temptbl1
// MAGIC Union Select entries2[16].Text Description, entries2[17].Text Quatity, entries2[18].Text UnitPrice, entries2[19].Text amount  from temptbl1

// COMMAND ----------

val sqlDF = spark.sql("SELECT Description,  cast(regexp_replace(Quatity, ',', '') as int) as Quatity, cast(regexp_replace(UnitPrice, ',', '') as int) as UnitPrice, cast(regexp_replace(amount, ',', '') as int) as amount FROM dailyOrders")

// COMMAND ----------

display(sqlDF)

// COMMAND ----------

val blobStorage = "<StorageName>.blob.core.windows.net"
val blobContainer = "<ContainerName>"
val blobAccessKey =  "oq1SJtsp2hkBKQNQqmYC9rWJ80926j3o5/bsjL/kbo+v8UtIUmxnQEDnQ0wf89j3Ks1MRVojkZBCGDO5MoV+7Q=="

// COMMAND ----------

val tempDir = "wasbs://" + blobContainer + "@" + blobStorage +"/tempDirs"

// COMMAND ----------

val acntInfo = "fs.azure.account.key."+ blobStorage
sc.hadoopConfiguration.set(acntInfo, blobAccessKey)

// COMMAND ----------

//SQL Data Warehouse related settings
val dwDatabase = "mpreporting"
val dwServer = "mpedw.database.windows.net"
val dwUser = "<username>"
val dwPass = "<password>"
val dwJdbcPort =  "1433"
val dwJdbcExtraOptions = "encrypt=true;trustServerCertificate=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"
val sqlDwUrl = "jdbc:sqlserver://" + dwServer + ":" + dwJdbcPort + ";database=" + dwDatabase + ";user=" + dwUser+";password=" + dwPass + ";$dwJdbcExtraOptions"
val sqlDwUrlSmall = "jdbc:sqlserver://" + dwServer + ":" + dwJdbcPort + ";database=" + dwDatabase + ";user=" + dwUser+";password=" + dwPass

// COMMAND ----------

spark.conf.set(
    "spark.sql.parquet.writeLegacyFormat",
    "true")

sqlDF.write
    .format("com.databricks.spark.sqldw")
    .option("url", sqlDwUrlSmall) 
    .option("dbtable", "DailyProduction")
    .option( "forward_spark_azure_storage_credentials","True")
    .option("tempdir", tempDir)
    .mode("overwrite")
    .save()

// COMMAND ----------

val listdf = dbutils.fs.ls("wasbs://<ContainerName>@<StorageName>.blob.core.windows.net/")

// COMMAND ----------

display(listdf)

// COMMAND ----------

listdf.foreach{x=>
  dbutils.fs.mv("wasbs://<ContainerName>@<StorageName>.blob.core.windows.net/" + x.name,"wasbs://<ArchiveContainer>@<StorageName>.blob.core.windows.net/")}
