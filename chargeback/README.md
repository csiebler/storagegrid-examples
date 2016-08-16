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
# Output

For each run, the script outputs a file with the name `usage_stats_yyyy-mm-dd_hh-mm-ss.csv` with content in the following formatt:
```
Account ID,Account Name,Used bytes,Used objects
94352640785552504147,Insight Demo,9887711905858390,409114
48885549006622297497,test,3775185426,52409712
57347988074919328399,insight-swift-demo,2758177842,144
```
