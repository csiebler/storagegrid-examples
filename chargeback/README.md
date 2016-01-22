= Introduction =
`chargeback.py` is a simple script that outputs a csv list with usage details for each account in StorageGRID Webscale in order to perform chargeback.
It can be used to chargeback users, e.g., by running the script daily and calculating a monthly average for billing purposes.

= Configuration =
Create a file under `~/.storagegrid_admin` which includes the credentials to the StorageGRID Webscale Admin node:
```
[config]
hostname = <hostname/IP of the StorageGRID Admin Node> 
username = <Username>
password = <Password>
```
