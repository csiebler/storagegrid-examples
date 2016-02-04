# curl examples

**Please do not use the `--insecure` option in production!**
The full API Docs can be found under: `https://<Admin Node Address>/ui/apidocs.html?url=/grid-api/v1-grid-combined-schema.yml`

## Authentication

```
curl -X POST -d '{"username":"Vendor", "password":"supersecret"}' \
             https://10.65.57.175/api/v1/authorize \
             --insecure
```

Reponse (`data` contains the authorization bearer token):
```
{
  "responseTime":"2016-02-04T13:31:31.363Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiMSIsImNyZWF0ZWQiOjE0NTQ1OTI2OTEsInJhbmRvbSI6NDcyNTkyfQ.vAM0F5L7xXee6XBfDL31J7TSNzOeKXamKQ54iPHkrCo"
}
```

## Creating an new Storage Account
A Storage Account is a container that contains multiple S3 or Swift accounts.

```
curl -X POST -H "Authorization: Bearer $TOKEN" \
             -d  '{"name": "Testuser","capabilities":["s3"]}' \
             https://10.65.57.175/api/v1/grid/accounts \
             --insecure
```

Response:
```
{
  "code":201,
  "responseTime":"2016-02-04T13:36:06.594Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{
    "id":"38569884310722871855",
    "name":"Testuser",
    "capabilities":[
      "s3"
    ]
  }
}
```

## Listing all Storage Accounts

```
curl -X GET -H "Authorization: Bearer $TOKEN" \
            https://10.65.57.175/api/v1/grid/accounts \
            --insecure
```

Response:
```
{
  "responseTime":"2016-02-04T13:36:56.661Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":[
    {
      "id":"48764205109923185543",
      "name":"another account",
      "capabilities":[
        "management",
        "s3"
      ]
    },
    {
      "id":"38569884310722871855",
      "name":"Testuser",
      "capabilities":[
        "s3"
      ]
    },
  ]
}
```

## Create S3 Account within Storage Account

```
curl -X POST -H "Authorization: Bearer $TOKEN" \
            -d '{}' \
            https://10.65.57.175/api/v1/grid/accounts/38569884310722871855/s3-access-keys \
            --insecure
```

Response:
```
{
  "code":201,
  "responseTime":"2016-02-04T13:42:51.104Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{
    "id":"SGKH-6Tt7o-S3Z2NdlXp6XAattsmCRgr8RoV2iN5yQ==",
    "accessKey":"Q84L100RVJLRH74O9QBE",
    "secretAccessKey":"PPIFs4Bixxxxxxxxxxxxxxxxxxxx",
    "displayName":"****************9QBE",
    "accountId":"38569884310722871855",
    "userURN":"urn:sgws:identity::38569884310722871855:root",
    "userUUID":"00000000-0000-0000-0000-000000000000",
    "expires":null
  }
}
```

## Get Storage Account Usage Details

```
curl -X GET -H "Authorization: Bearer $TOKEN" \
            https://10.65.57.175/api/v1/grid/accounts/38569884310722871855/usage \
            --insecure
```

Response:
```
{
  "responseTime":"2016-02-04T14:03:41.703Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{
    "buckets":[

    ],
    "calculationTime":"2016-02-04T14:03:41.693Z",
    "objectCount":351824,
    "dataBytes":8942998425781
  }
}
```
