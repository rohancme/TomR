# TomR
Voldemort's unevolved form

A distributed key-value store using the Amazon Dynamo White paper and it's open source implementation (<a href="http://www.project-voldemort.com/voldemort/">Voldemort</a>) as references.

## Setting up the System
1. Set the environment variable TOMR_PROPS to the path to your tomr.config file
2. Run NodeStarter.java on all nodes which are to be used as request servicers and data stores
3. Start LoadBalancer.java on the system that you plan to use as the Load Balancer
4. Done!

## Adding or deleting a new node in the System
1. Use the sample code in Admin.java to generate requests for either of this functionality

## Client Requests
1. Currently InitializeClient.java and NodeClientRequestServicer.java provide sample code for how you can query the database.

##Monitoring the System:
1. Clone https://github.com/rchakra3/TomROverview:
   git clone "https://github.com/rchakra3/TomROverview"
   cd TomROverview
2. On the monitoring node:
   npm install --save
   node app.js
2. On all the datanodes:
   cd client
   npm install --save
   node app.js
  
