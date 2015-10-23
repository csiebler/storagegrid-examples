import swiftclient

username = '80180975951696906883:swiftadmin'
password = 'supersecret'
authurl = 'https://10.65.57.176:8083/auth/v1.0'

swift = swiftclient.client.Connection(auth_version='1', user=username, key=password, insecure=True, authurl=authurl)

'''
If you are using self-signed certificates, you can use the following code:

cacert = '/path/to/server/cert'
swift = swiftclient.client.Connection(auth_version='1', user=username, key=password, cacert=cacert, authurl=authurl)
'''

# Get authentication infomration
print(swift.get_auth())

# Create container
swift.put_container('test-container')

# List containers and account information
response = swift.get_account()
account_info = response[0]
containers = response[1]

print "Bytes used by account: ", account_info['x-account-bytes-used']
print "Number of containers in account: ", account_info['x-account-container-count']
print "Number of objects in account: ", account_info['x-account-object-count']

for c in containers:
    name = c['name']
    num_objects = c['count']
    size = c['bytes']
    print("Container name: %s (total: %s objects, %s bytes)" % (name, num_objects, size))

# Put object into store
swift.put_object('test-container', 'test-object', 'This is my object\'s content.')

# Get object from store
response = swift.get_object('test-container', 'test-object')
object_headers = response[0]
object_content = response[1]
print "Object headers: ", object_headers
print "Object content: ", object_content

# Delete object
swift.delete_object('test-container', 'test-object')

# Delete container
swift.delete_container('test-container')

