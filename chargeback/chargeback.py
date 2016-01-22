import requests
import json
import csv
import time
from os.path import expanduser
try:
    import configparser
except ImportError:
    import ConfigParser as configparser

# Configuration
filename = 'usage_stats_' + time.strftime("%Y-%m-%d_%H-%M-%S") + '.csv'

parser = configparser.ConfigParser()
parser.read(expanduser('~') + '/.storagegrid_admin')
hostname = parser.get('config', 'hostname')
username = parser.get('config', 'username')
password = parser.get('config', 'password')

base_url = 'https://' + hostname + '/api/v1'

# Get Authorization token
data = json.dumps({'username': username, 'password': password}) 
req = requests.post(base_url + "/authorize", data, verify=False)
token = json.loads(req.text)['data']

# Get all accounts in the GRID
req = requests.get(base_url + "/grid/accounts", headers={'Authorization': 'Bearer ' + token}, verify=False)
accounts = req.json()['data']

# Get usage for all accounts
stats = []
for _ in accounts:  
    id = _['id']  
    name = _['name']  
    req = requests.get(base_url + "/grid/accounts/" + id + "/usage", headers={'Authorization': 'Bearer ' + token}, verify=False)
    usage = req.json()['data']
    bytes_used = str(usage['dataBytes'])
    objects_used = str(usage['objectCount'])
    stats.append({'name': name, 'id': id, 'bytes_used': bytes_used, 'objects_used': objects_used});

# Write output to csv
f = open(filename, 'wt')
try:
    writer = csv.writer(f)
    writer.writerow(('Account ID', 'Account Name', 'Used bytes', 'Used objects'))
    for _ in stats:
        writer.writerow((_['id'], _['name'], _['bytes_used'], _['objects_used']))
finally:
    f.close()

