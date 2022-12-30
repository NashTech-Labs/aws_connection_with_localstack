package com.knoldus.aws.aws

import com.amazonaws.auth._

object CredentialsLookup {

  /**
    * Returns AWS credentials based on access key and secret key
    *
    * @param accessKey Access key
    * @param secretKey Secret key
    * @return An AWSCredentialsProvider
    */
  def getCredentialsProvider(
    accessKey: String,
    secretKey: String
  ): AWSCredentialsProvider = {
    val provider: AWSCredentialsProvider = {
      if (isDefault(accessKey) && isDefault(secretKey))
        new DefaultAWSCredentialsProviderChain()
      else if (isDefault(accessKey) || isDefault(secretKey))
        throw new RuntimeException("access-key and secret-key must both be set to 'default', or neither")
      else
        new BasicAWSCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))
    }
    provider
  }

  /**
    * Is the access/secret key set to the special value "default" i.e. use
    * the standard provider chain for credentials.
    *
    * @param key The key to check
    * @return true if key is default, false otherwise
    */
  private def isDefault(key: String): Boolean = key == "default"

  // Wrap BasicAWSCredential objects.
  class BasicAWSCredentialsProvider(basic: BasicAWSCredentials) extends AWSCredentialsProvider {
    @Override def getCredentials: AWSCredentials = basic

    @Override def refresh(): Unit = {
      //empty method: basically this method we can override the functionality to force this credential provider to
      //refresh its credentials.
    }
  }
}
