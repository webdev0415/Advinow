/*package com.advinow.mica.aws;


 
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

*//**
 * 
 * @author Govinda Reddy
 *
 *//*
public class AwsS3Config {


	public AmazonS3 s3client(String awsId,String awsKey,String region) {
		 
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
								.withRegion(Regions.fromName(region))
		                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		                        .build();
		
		return s3Client;
	}
}
*/