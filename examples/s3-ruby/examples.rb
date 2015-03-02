#!/usr/bin/env ruby
require 'aws-sdk'

endpoint = 'https://10.65.57.176:8082'
credentials = Aws::SharedCredentials.new(profile_name: 'my_profile')


s3 = Aws::S3::Client.new(region: 'us-east-1',
                         endpoint: endpoint,
			 credentials: credentials,
			 force_path_style: true,
                         ssl_verify_peer: false,
#                         ssl_ca_bundle: 'server_cert.crt'
                         )

# List buckets
list_response = s3.list_buckets
list_response.buckets.each do |bucket|
    puts "Bucket: #{bucket.name} (created: #{bucket.creation_date})"
end

# Create bucket
s3.create_bucket(bucket: 'new-bucket')

# Delete bucket
s3.delete_bucket(bucket: 'new-bucket')

# Create Object
s3.put_object(bucket: 'test',
              key: 'my_object',
	      metadata: {'myvalue' => 'mykey'},
	      body: 'this will be in the object')

# List objects
list_response = s3.list_objects(bucket: 'test')
list_response.contents.each do |object|
    puts "Object: #{object.key} (size: #{object.size}, last modified: #{object.last_modified})"
end

# Get object
get_response = s3.get_object(bucket: 'test', key: 'my_object')
puts "Object content: #{get_response.body.string}"

# Get object metadata
head_response = s3.head_object(bucket: 'test', key: 'my_object')
puts "Object metadata: #{head_response.metadata}"

# Delete object
s3.delete_object(bucket: 'test', key: 'my_object')
