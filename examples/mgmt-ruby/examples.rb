#!/usr/bin/env ruby
require 'rest_client'

# Configuration
hostname = '10.65.57.175'
username = 'Vendor'
password = 'supersecret'

endpoint = "https://#{hostname}/api/v1"

def convert_and_print_response (response)
  response = JSON.parse(response)
  puts "Response: #{response}"
  response
end

puts "\nRequesting authorization token..."

client = RestClient::Resource.new(endpoint + '/authorize', :verify_ssl => false)
data = "username=#{username}&password=#{password}"
response = client.post(data)
response = convert_and_print_response(response)
token =  response['data']
puts "Token: #{token}"

# Setup RestClient with authorization token
client = RestClient::Resource.new(endpoint + '/service-provider/s3-accounts', :verify_ssl => false, :headers => { 'Authorization' => "Bearer #{token}" })

puts "\nCreating S3 Account..."
request = {'name' => 'testaccount'}
response = client.post(request.to_json)
response = convert_and_print_response(response)

account_details = response['data']
access_key = account_details['accessKey']
secret_key = account_details['secretAccessKey']
puts "New account: #{access_key}, #{secret_key}"

puts "\nListing S3 Accounts..."
response = client.get
response = convert_and_print_response(response)

accounts = response['data']
puts "Accounts:"
puts accounts

puts "\nGetting S3 Account details..."
# Get account id of newly created account
account_id = accounts.select{|a| a['name'] == 'testaccount'}.first['id']
response = client['/' + account_id].get
convert_and_print_response(response)

puts "\nGetting S3 Account usage..."
response = client['/' + account_id  + '/usage'].get
response = convert_and_print_response(response)
account_usage = response['data']
object_count = account_usage['objectCount']
total_size = account_usage['dataBytes']
puts "Total account usage: #{object_count} objects, #{total_size} bytes"

puts "\nRegenerating S3 Account Secret Access Key..."
response = client['/' + account_id  + '/regenerate-keys'].post('')
response = convert_and_print_response(response)

account_details = response['data']
access_key = account_details['accessKey']
secret_key = account_details['secretAccessKey']
puts "Updated account: #{access_key}, #{secret_key}"

puts "\nUpdating S3 Account name..."
request = {'name' => 'newtestaccountname'}
response = client['/' + account_id].patch(request.to_json)
convert_and_print_response(response)

puts "\nDeleting S3 Account..."
response = client['/' + account_id].delete
convert_and_print_response(response)

