package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader (new InputStreamReader (System.in)); //create a BufferedReader to read user input
        URL url;    //create a variable to store the url
        System.out.println("Enter the initials of the staff to look up.");
        String inputString = input.readLine();  //read the user's input


        try{
            url = new URL("https://www.ecs.soton.ac.uk/people/" + inputString); //initialise the URL variable
        } catch(MalformedURLException e){ //check that the url is valid and if not, print the error and exit
            e.printStackTrace();
            return;
        }

        URLConnection con = url.openConnection(); //open a connection to the url
        InputStream is = con.getInputStream();  //create the input stream
        BufferedReader br = new BufferedReader(new InputStreamReader(is)); //create the bufferedreader from the input stream

        String line = null; //create a variable to store the current line
        while((line = br.readLine()) != null){  //iterate through each line of the input
            if(line.indexOf("property=\"name\"") != -1){    //if the line contains property="name"
                String temp = line.substring(line.indexOf("property=\"name\">") + 16); //cut off everything before the name
                String name = temp.substring(0, temp.indexOf('<')); //cut off everything after the name
                System.out.println(name); //print the name
            }
        }


    }
}
