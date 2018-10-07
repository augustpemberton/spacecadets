package com.company;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.lang3.StringEscapeUtils;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader (new InputStreamReader (System.in)); //create a BufferedReader to read user input
        URL url;                                                //create a variable to store the url
        System.out.println("Enter the name of the artist.");
        String inputString = input.readLine();                  //read the user's input

        inputString = inputString.replaceAll(" ", "+");         //make the input string match the website's url format
        String urlstring = "https://www.last.fm/music/"+inputString+"/+similar";

        try{
            url = new URL(urlstring);                           //initialise the URL variable
        } catch(MalformedURLException e){                       //check that the url is valid and if not, print the error and exit
            e.printStackTrace();
            return;
        }

        URLConnection con = url.openConnection();               //open a connection to the url
        InputStream is;
        try{
            is = con.getInputStream();                          //create the input stream
        } catch(FileNotFoundException e){
            System.out.println("Artist not found.");
            return;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is)); //create the BufferedReader from the input stream

        String line = null;                             //create a variable to hold each line
        while((line = br.readLine()) != null){          //iterate through each line of the web page
            if(line.contains("itemprop=\"url\"")){      //all names have this string before them
                Boolean hasFound = false;               //create a a boolean to hold the state of whether we found the name yet
                while(!hasFound){                       //iterate through until we find the name
                    line = br.readLine();                   //read the next line
                    if(line.contains("</a></h3>")){         //if we found the name
                        hasFound = true;                    //say we found the name and exit the loop
                    } else if(line.contains("</html>")) {   //if we reach the end of the document, exit
                        return;
                    }
                }
                String temp = line.substring(line.indexOf(">") + 1); //remove everything before the name
                String name = temp.substring(0, temp.indexOf('<')); //remove everything after the name
                System.out.println(StringEscapeUtils.unescapeHtml4(name)); //unescape the html characters and print
            }
        }



    }
}
