# Introduction
`chargeback.py` is a simple script that outputs a csv list with usage details for each account in StorageGRID Webscale. This data can be used for billing purposes.
It is recommended to run the script daily and calculate the monthly average for a more accurate consumption report.

# Configuration
Create a file `~/.storagegrid_admin` which includes the address and credentials to the StorageGRID Webscale Admin node:
```
[config]
hostname = <hostname/IP of the StorageGRID Admin Node> 
username = <Username>
password = <Password>
```
