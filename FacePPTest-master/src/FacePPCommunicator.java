/**
 * Created by User on 4/28/2016.
 * Joey McMahon
 * Keith Hamm
 * I got a lot of help from Keith's code in this. I rewrote all the methods he wrote, though I made some minor changes in
 * some cases. Most methods look extremely similar; this is due to there being not much room for creativity in using
 * the facepp API. The code should look more different once I implement for my team's architecture.
 */
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import java.io.File;

/**
 * Facilitates communication between the authentication server and the FacePlusPlus API.
 * Makes HTTP requests to the API to create groups of people. Each person has a set of face
 * images. These are used to determine if a provided image of a person's face matches any
 * of the people in the group.
 */
public class FacePPCommunicator {

    private String apiKey;
    private String apiSecret;
    private Boolean useChineseServer;
    private Boolean useHttp;
    private HttpRequests httpRequests;
    private String groupName;

    /**
     * Constructor
     */
    public FacePPCommunicator(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        useChineseServer = false;
        useHttp = true;
        groupName = "faceSqaud";

        initializeHttpRequests();
    }

    /**
     * Initializes the HTTP request object with the API key and API secret.
     */
    private void initializeHttpRequests() {
        httpRequests = new HttpRequests(apiKey, apiSecret, useChineseServer, useHttp);
    }

    /**
     * Attempts to detect a face in the image at the given URL.
     * Returns the face ID associated with the face in the image
     */
    protected String detectFace(String url){
        System.out.println("detecting face in FacePP with url:" + url +"\n");
        JSONObject result = new JSONObject();
        try {
            result = httpRequests.detectionDetect(new PostParameters().setUrl(url));
        } catch (FaceppParseException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return getFaceId(result);
    }


    /**
     * Attempts to detect a face in the image file.
     * Why can't we extract face_id from the JSONObject in this case?
     * Do we need to?
     */
    protected String detectFace(File file){
        System.out.println("detecting face in FacePP" + "\n");
        JSONObject result = new JSONObject();
        try {
            result = httpRequests.detectionDetect(new PostParameters().setImg(file));
        }
        catch (FaceppParseException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result.toString();
    }

    /**
     * Attempts to detect a face in the image byte array.
     * Again, why can't we extract face_id from the JSONObject this time?
     * Do we need to though?
     */
     protected String detectFace(byte[] data) {
         System.out.println("detecting face in FacePP" + "\n");
         JSONObject result = new JSONObject();
         try {
             result = httpRequests.detectionDetect(new PostParameters().setImg(data));
         }
         catch (FaceppParseException e) {
             e.printStackTrace();
         }
         System.out.println(result);
         return result.toString();
     }

         /**
          * Creates a person with the given name and adds them to the existing group.
          * We want to do this since for each user, only one group will be necessary, and since
          * each user gets its own instance of FacePPCommunicator, so there shouldn't be any issues
          * implementing this method this way.
          */
          protected String createPerson(String personName) {
              System.out.println("Creating person on FACEPP: " + personName + "\n");
              JSONObject result = new JSONObject();
              try {
                  result = httpRequests.personCreate(new PostParameters().setPersonName(personName));
              }
              catch (FaceppParseException e) {
                  e.printStackTrace();
              }
              System.out.println(result);
              String personID = null;
              personID =  getFaceId(result);
              addPerson(personID);
              return personID;
          }

          /**
           * Removes a person from the group using the person's name.
           * Keith used personDelete(). I wonder if this does the same thing, since
           * when he adds a person, he adds that person to the group using groupAddPerson. Tbe question is,
           * does one need to use both personDelete() and groupRemovePerson() or just one?
           */
          protected String removePerson(String personName) {
          System.out.println("Removing person: " + personName + "\n");
          JSONObject result = new JSONObject();
          try {
              result = httpRequests.groupRemovePerson(new PostParameters().setGroupId(groupName).setPersonName(personName));
          }
          catch (FaceppParseException e) {
              e.printStackTrace();
          }
          return result.toString();
          }

          /**
          * Gets the face ID from the given result.
          *
          */
         protected String getFaceId (JSONObject result){
             String faceId = "";

             try {
                 faceId = result.getJSONArray("face").getJSONObject(0).getString("face_id");
             } catch (JSONException e) {
                 e.printStackTrace();
             }

             return faceId;
         }


         /**
          * Adds a face to a person using the person's ID and the face ID.
          */
        protected String addFace(String personID, String faceID) {
            System.out.println("adding face to person: " + personID + "\n");
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.personAddFace(new PostParameters().setPersonId(personID).setFaceId(faceID));
            } catch (FaceppParseException e) {
                e.printStackTrace();
            }
            return result.toString();
        }

          /**
          * Removes a face from a person using the person's ID and the face ID.
          */
        protected String removeFace(String personName, String faceID){
            System.out.println("Removing face from person: " + personName);
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.personRemoveFace(new PostParameters().setPersonId(personName).setFaceId(faceID));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            return result.toString();
        }



          /**
          * Creates a person group.
          */
        protected String creatGroup(String groupName){
            System.out.println("[facepp] Creating group: " + groupName + "\n");
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.groupCreate(new PostParameters().setGroupName(groupName));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            return result.toString();
        }

          /**
          * Removes an existing person group.
          */
        protected String removeGroup(String groupName){
        JSONObject result = new JSONObject();
        try {
            result = httpRequests.groupDelete(new PostParameters().setGroupName(groupName));
        } catch (FaceppParseException e){
            e.printStackTrace();
        }
        return result.toString();
        }

          /**
          * Adds a person to a group with the person's ID.
          */
          private String addPerson(String personName){
              System.out.println("Adding person to group: " + personName + "\n");
              JSONObject result = new JSONObject();
              try {
                  result = httpRequests.groupAddPerson(new PostParameters().setGroupId(groupName).setPersonId(personName));
              }
              catch (FaceppParseException e) {
                  e.printStackTrace();
              }
              System.out.println(result);
              return result.toString();
          }


        /**
          * Face trains a person in preparation for identification.
          */
        protected String trainPerson(String personName){
            System.out.println("Training Person" + personName + "\n");
            JSONObject result = new JSONObject();
            try {
                result  = httpRequests.trainIdentify(new PostParameters().setPersonName(personName));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            return result.toString();
        }

    /**
     * Face trains a group of people.
     * @return
     */
    protected String trainGroup(){
        System.out.println("Training group: " + groupName + "\n");
        JSONObject result = new JSONObject();
        JSONObject sessionInfo;
        try {
            result = httpRequests.trainIdentify(new PostParameters().setGroupName(groupName));
            sessionInfo = httpRequests.infoGetSession(new PostParameters().setSessionId(result.toString()));
            return sessionInfo.toString();
        } catch (FaceppParseException e){
            e.printStackTrace();
        }
        return "UNSUCCESFULL RETRIEVAL OF SESSION INFO FOR TRAINING";
    }



        /**
          * Attempts to identify the person in the group in the image at the given URL
          */
        protected String identifyPerson(String url){
            System.out.println("Identifying person in url: " + url + "\n");
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.trainIdentify(new PostParameters().setGroupName(groupName).setUrl(url));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            return result.toString();
        }

        /**
        * Attempts to identify the person in the given image file.
        */
        protected String identifyPerson(File file){
            System.out.println("Identifying person in file: " + file + "\n");
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.trainIdentify(new PostParameters().setGroupName(groupName).setImg(file));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            return result.toString();
        }

          /**
          * Attempts to identify the person in the given image byte array.
          */
         protected String identifyPerson(byte[] data){
             System.out.println("Identifying person in data: " + data + "\n");
             JSONObject result = new JSONObject();
             try {
                 result = httpRequests.trainIdentify(new PostParameters().setGroupName(groupName).setImg(data));
             } catch (FaceppParseException e){
                 e.printStackTrace();
             }
             return result.toString();
         }


    public static void main(String[] args) {

        String apiKey = "FIX ME";
        String apiSecret = "FIX ME";
        FacePPAPICommunicator fpp = new FacePPAPICommunicator(apiKey, apiSecret);
        /*
        fpp.createGroup();

        String personId = fpp.createPerson("person_0");

        fpp.addFace(personId,
                "https://www.whitehouse.gov/sites/whitehouse.gov/files/images/first-family/44_barack_obama%5B1%5D.jpg");

        fpp.addFace(personId,
                "http://www.worldnewspolitics.com/wp-content/uploads/2016/02/140718-barack-obama-2115_86aea53294a878936633ec10495866b6.jpg");

        fpp.addFace(personId,
                "http://i2.cdn.turner.com/cnnnext/dam/assets/150213095929-27-obama-0213-super-169.jpg");

        fpp.addFace(personId,
                "http://a.abcnews.go.com/images/US/AP_obama8_ml_150618_16x9_992.jpg");

        fpp.addFace(personId,
                "http://www.dailystormer.com/wp-content/uploads/2015/06/2014-10-12-obama-618x402.jpg");

        fpp.addFace(personId,
                "http://media.vocativ.com/photos/2015/10/RTS2O4I-22195838534.jpg");

        fpp.addFace(personId,
                "https://upload.wikimedia.org/wikipedia/commons/e/e9/Official_portrait_of_Barack_Obama.jpg");

        fpp.addFace(personId,
                "http://cbsnews2.cbsistatic.com/hub/i/r/2015/10/09/f27caaea-86d1-41e2-bec2-97e89e5dba03/thumbnail/770x430/0a4b24d154ee526bb6811f1888e16600/presidentobamamain.jpg");

        fpp.trainGroup();

        fpp.identifyPerson("http://i.huffpost.com/gen/2518262/images/n-OBAMA-628x314.jpg");

        fpp.removePerson(personId);
        */
    }
}
