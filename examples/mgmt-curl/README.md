# curl examples

**Please do not use the `--insecure` in production!**

## Authentication

```
curl -X POST -H "Content-Type: multipart/form-data"
             -F "username=Vendor" -F "password=supersecret"
             https://10.65.57.175/api/v1/authorize
             --insecure
```

Reponse:

```
{  
  "responseTime":"2015-03-19T08:41:06.544Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJjcmVhdGVkIjoxNDI2NzU0NDY2LCJyYW5kb20iOjE5NDY1Nn0.w_VaD3lY5tbQtsTKEoj4VDysD_SzGYtj-95czO2bU0w"
}
```

## Creating an S3 Account

```
curl -X POST -H "Authorization: Bearer $TOKEN" 
             -d  '{"name":"foobar"}' 
             https://10.65.57.175/api/v1/service-provider/s3-accounts
             --insecure
```

Response:
```
{  
  "responseTime":"2015-03-19T08:48:07.943Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{  
    "id":"b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3",
    "name":"foobar",
    "accessKey":"5DQOHNN9Q2DEYH06VNGY",
    "secretAccessKey":"pi83p8VThHKXjjXo7Bgd0UMk4ITvFbNk6jEejwF5"
  }
}
```

## Listing S3 Accounts

```
curl -X GET -H "Authorization: Bearer $TOKEN"
            https://10.65.57.175/api/v1/service-provider/s3-accounts
            --insecure
```
Response:
```
{  
  "responseTime":"2015-03-19T08:49:19.652Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":[  
    {  
      "id":"b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3",
      "name":"foobar",
      "accessKey":"5DQOHNN9Q2DEYH06VNGY"
    },
    {  
      "id":"cb09525c1f6f16b1b84b1a01a92eee4489a056c6e3b8b3e497bfc31d1ef13a81",
      "name":"clemens",
      "accessKey":"HBVGOK0PTI0PG3RLVTSX"
    }
  ]
}
```

## Get S3 Account Details

```
curl -X GET -H "Authorization: Bearer $TOKEN"
            https://10.65.57.175/api/v1/service-provider/s3-accounts/b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3
            --insecure
```
Response:
```
{  
  "responseTime":"2015-03-19T08:50:57.798Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{  
    "id":"b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3",
    "name":"foobar",
    "accessKey":"5DQOHNN9Q2DEYH06VNGY"
  }
}
```

## Get S3 Account Usage Details

```
curl -X GET -H "Authorization: Bearer $TOKEN" 
            https://10.65.57.175/api/v1/service-provider/s3-accounts/b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3/usage
            --insecure
```
Response:
```
{  
  "responseTime":"2015-03-19T08:52:03.549Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{  
    "buckets":[  
      {  
        "name":"mybucket123",
        "objectCount":1659,
        "dataBytes":4704064672
      }
    ],
    "calculationTime":"2015-03-19T08:51:51.853Z",
    "objectCount":1659,
    "dataBytes":4704064672
  }
}
```

## Update an S3 Account

```
curl -X PATCH -H "Authorization: Bearer $TOKEN"
              -d '{"name":"newfoobar"}'
              https://10.65.57.175/api/v1/service-provider/s3-accounts/b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3
              --insecure
```
Response:
```
{  
  "responseTime":"2015-03-19T08:53:50.958Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{  
    "id":"b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3",
    "name":"newfoobar",
    "accessKey":"5DQOHNN9Q2DEYH06VNGY"
  }
}
```

## Regenerate the access key and access secret key for an S3 Acccount

```
curl -X POST -H "Authorization: Bearer $TOKEN" -d '' https://10.65.57.175/api/v1/service-provider/s3-accounts/b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3/regenerate-keys --insecure
```
Response:
```
{  
  "responseTime":"2015-03-19T08:55:15.709Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{  
    "id":"b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3",
    "name":"newfoobar",
    "accessKey":"UP3TDBGF2VYH81LQUMWK",
    "secretAccessKey":"mf/RAii4hNFiu9cs7VT8jk/anuTDMy+KPfaWP1eQ"
  }
}
```

## Delete an S3 Account

```
curl -X DELETE -H "Authorization: Bearer $TOKEN"
               https://10.65.57.175/api/v1/service-provider/s3-accounts/b282236abd3f6f5fc1a79c728c44f11205ecd56499daab04e9879b60dd5cd9c3
               --insecure
```
Response:
```
{  
  "responseTime":"2015-03-19T08:56:07.864Z",
  "status":"success",
  "apiVersion":"1.0",
  "data":{  

  }
}
```
