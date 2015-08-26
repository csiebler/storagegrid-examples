#!/usr/bin/env ruby
require 'aws-sdk'

endpoint = 'https://10.65.57.176:8082'
credentials = Aws::SharedCredentials.new(profile_name: 'my_profile')

# Notes on certificate usage with StorageGRID Webscale
# ----------------------------------------------------
# Set ssl_verfiy_peer to true if StorageGRID's certificate is CA-signed or if you want to use a self-signed certificate
# If you use a self-signed certificate, set ssl_ca_bundle to the self-signed certificate

s3 = Aws::S3::Client.new(region: 'us-east-1',
    endpoint: endpoint,
    credentials: credentials,
    force_path_style: true,
    ssl_verify_peer: false,
    #ssl_ca_bundle: 'server_cert.crt'
)

# Bucket related operations
# -------------------------

# List buckets
list_response = s3.list_buckets
puts "Bucket Owner Name: #{list_response.owner.display_name}"
puts "Bucket Owner ID: #{list_response.owner.id}"
list_response.buckets.each do |bucket|
    puts "Bucket: #{bucket.name}"
    puts " -> created: #{bucket.creation_date}"
end

# Create bucket
s3.create_bucket(bucket: 'new-bucket')

# Delete bucket
s3.delete_bucket(bucket: 'new-bucket')

# Object related operations
# -------------------------

# Create Object
s3.put_object(bucket: 'test',
    key: 'my_object',
    metadata: {
        'mykey1' => 'myvalue1',
        'mykey2' => 'myvalue2'
    },
    body: 'Hello, I\'m the object\'s data!',
    # encrypt object if desired
    server_side_encryption: 'AES256'
)

# Copy existing Object
s3.copy_object(bucket: 'test',
    key: 'copied_object',
    copy_source: '/test/my_object'
)

# List objects
list_response = s3.list_objects(bucket: 'test')
list_response.contents.each do |object|
    puts "Object key: #{object.key}"
    puts " -> Size: #{object.size} bytes"
    puts " -> Last modified: #{object.last_modified}"
end

# Get object
get_response = s3.get_object(bucket: 'test', key: 'my_object')
puts "Object content: #{get_response.body.string}"

# Get object metadata
head_response = s3.head_object(bucket: 'test', key: 'my_object')
puts "Object metadata: #{head_response.metadata}"
puts "Object content length: #{head_response.content_length}"
puts "Object last modified: #{head_response.last_modified}"

# Delete object
s3.delete_object(bucket: 'test', key: 'my_object')
