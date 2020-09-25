package com.aws.wj;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

import java.io.File;
import java.io.IOException;


public class GlacierApplication{

    public final static String VAULT_NAME_ARG="--vault-name";
    public final static String INPUT_FILE_PATH_ARG="--input-file-path";
    public final static String ACCOUNT_ID="--account-id";
    public final static String DESCRIPTION="--description";
    public static AmazonGlacierClient client;


    public static void main(String[] args) throws IOException {

        ProfileCredentialsProvider credentials = new ProfileCredentialsProvider();
        client = new AmazonGlacierClient(credentials);
        client.setEndpoint("https://glacier.us-east-2.amazonaws.com/");

        try {
            ArchiveTransferManager atm = new ArchiveTransferManager(client, credentials);
            String vaultName = getArgument(args,VAULT_NAME_ARG);
            String pathToArchive = getArgument(args,INPUT_FILE_PATH_ARG);
            File fileToArchive = new File(pathToArchive);
            String accountId = getArgument(args,ACCOUNT_ID);
            String description = getArgument(args,DESCRIPTION);

            Long fileSize=fileToArchive.length();

            System.out.println("-----------------------------------------------------------------");
            System.out.println(String.format("Upload %s to %s",pathToArchive,vaultName));
            System.out.println("-----------------------------------------------------------------");


            UploadResult result = atm.upload(accountId,vaultName,description,fileToArchive,new ProgressListenerImpl(fileSize));
            System.out.println("Archive ID: " + result.getArchiveId());

        } catch (Exception e)
        {
            System.err.println(e);
        }
    }

    public static String getArgument(String[] args,String targetArg) throws Exception{
        for(int i=0;i<args.length;i++){
            if(!args[i].equals(targetArg)){
                continue;
            }
            if(!(i+1<args.length)){
                throw new ArgumentErrorException(targetArg);
            }
            if(args[i+1].isEmpty()){
                throw new ArgumentErrorException(targetArg);
            }
            return args[i+1];
        }
        throw new ArgumentErrorException(targetArg);
    }

    private static class ArgumentErrorException extends Exception{
        public ArgumentErrorException(String argument){
            super(String.format("Argument Error: %s is compulsory",argument));
        }
    }

}