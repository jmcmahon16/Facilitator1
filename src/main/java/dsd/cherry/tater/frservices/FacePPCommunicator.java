/**
 * Created by User on 4/28/2016.
 * Joey McMahon
 * Keith Hamm
 * I got a lot of help from Keith's code in this. I rewrote all the methods he wrote, though I made some minor changes in
 * some cases. Most methods look extremely similar; this is due to there being not much room for creativity in using
 * the facepp API. The code should look more different once I implement for my team's architecture.
 */
package dsd.cherry.tater.frservices;

import dsd.cherry.tater.frservices.FRServiceHandlerTrainResponse;
import dsd.cherry.tater.types.ImageData;
import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;

import dsd.cherry.tater.frservices.FRServiceHandler;


/**
 * Facilitates communication between the authentication server and the FacePlusPlus API.
 * Makes HTTP requests to the API to create groups of people. Each person has a set of face
 * images. These are used to determine if a provided image of a person's face matches any
 * of the people in the group.
 */
public class FacePPCommunicator extends FRServiceHandler {

    private String apiKey = "15bd4507cc5030eb330d4ab62ce5fd69";
    private String apiSecret = "LI5HIlv6RsvY_Si2t0GwE9NQZPGbuHW6";
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
        groupName = "faceSqaud2";

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
        System.out.println(result.toString());
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
        System.out.println(result.toString());
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
         System.out.println("result: " + result.toString() + "\n");
         return result.toString();
     }

         /**
          * Creates a person with the given name and adds them to the existing group.
          * We want to do this since for each user, only one group will be necessary, and since
          * each user gets its own instance of FacePPCommunicator, so there shouldn't be any issues
          * implementing this method this way.
          */
          protected String createPersonInGroup(String personName) {
              System.out.println("Creating person in group on FACEPP: " + personName + "\n");
              JSONObject result = new JSONObject();
              try {
                  result = httpRequests.personCreate(new PostParameters().setPersonName(personName));
                  System.out.println("result: " + result.toString() +  "\n");
              }
              catch (FaceppParseException e) {
                  e.printStackTrace();
              }
              String personID = null;
              try {
                  personID = result.getString("person_id");
              } catch (JSONException e) {
                  e.printStackTrace();
              }
              System.out.println("person ID: " + personID);
              addPersonToGroup(personID);
              return personID;
          }

    /**
     * @param personName An identifier for a given person. Doesn't have to be their actual name
     *                   Note: personID is different. This is assigned by facepp.
     * @return: the personID assigned by facepp. This is another way of communicating to facepp who we are dealing with.
     */
    protected String createPerson(String personName){
              System.out.println("Creating person in group on FACEPP: " + personName + "\n");
              JSONObject result = new JSONObject();
              try {
                  result = httpRequests.personCreate(new PostParameters().setPersonName(personName));
                  System.out.println("result: " + result.toString() +  "\n");
              }
              catch (FaceppParseException e) {
                  e.printStackTrace();
              }
              String personID = null;
              try {
                  personID = result.getString("person_id");
              } catch (JSONException e) {
                  e.printStackTrace();
              }
              System.out.println("person ID: " + personID);
              return personID;
          }

          /**
           * Removes a person from the group using the person's name.
           * Keith used personDelete(). I wonder if this does the same thing, since
           * when he adds a person, he adds that person to the group using groupAddPerson. Tbe question is,
           * does one need to use both personDelete() and groupRemovePerson() or just one?
           */
          protected String removePersonFromGroup(String personID) {
          System.out.println("Removing person from group: " + personID + "\n");
          JSONObject result = new JSONObject();
          try {
              result = httpRequests.groupRemovePerson(new PostParameters().setGroupName(groupName).setPersonId(personID));
          }
          catch (FaceppParseException e) {
              e.printStackTrace();
          }
          System.out.println("result: " + result.toString() + "\n");
          return result.toString();
          }

          protected String removePerson(String personID){
              System.out.println("Removing person: " + personID + "\n");
              JSONObject result = new JSONObject();
              try {
                  result = httpRequests.personDelete(new PostParameters().setPersonId(personID));
              }
              catch (FaceppParseException e) {
                  e.printStackTrace();
              }
              System.out.println("result: " + result.toString() + "\n");
              return result.toString();
          }

        protected String removePersonWName(String personName){
        System.out.println("Removing person: " + personName + "\n");
        JSONObject result = new JSONObject();
        try {
            result = httpRequests.personDelete(new PostParameters().setPersonName(personName));
        }
        catch (FaceppParseException e) {
            e.printStackTrace();
        }
        System.out.println("result: " + result.toString() + "\n");
        return result.toString();
        }

          /**
          * Gets the face ID from the given result.
          *
          */
         private String getFaceId (JSONObject result){
             String faceId = "";
             System.out.println("Retrieving face ID\n");
             try {
                 faceId = result.getJSONArray("face").getJSONObject(0).getString("face_id");
             } catch (JSONException e) {
                 e.printStackTrace();
             }
             System.out.println("faceId: " + faceId);
             return faceId;
         }


         /**
          * Adds a face to a person using the person's name and an image
          */
        protected String addFaceToPerson(String personID, String url) {
            System.out.println("adding face to person: " + personID + "\n");
            String faceID = detectFace(url); //gets the faceID associated with an image.
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.personAddFace(new PostParameters().setPersonId(personID).setFaceId(faceID));
            } catch (FaceppParseException e) {
                e.printStackTrace();
            }
            System.out.println("result: " + result.toString() + "\n");
            return result.toString();
        }

        protected String addFaceToPerson(String personID, byte[] data) {
            System.out.println("adding face to person: " + personID + "\n");
            String faceID = detectFace(data); //gets the faceID associated with an image.
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.personAddFace(new PostParameters().setPersonId(personID).setFaceId(faceID));
            } catch (FaceppParseException e) {
                e.printStackTrace();
            }
            System.out.println("result: " + result.toString() + "\n");
            return result.toString();
        }

        protected String addFaceToPerson(String personID, File file) {
            System.out.println("adding face to person: " + personID + "\n");
            String faceID = detectFace(file); //gets the faceID associated with an image.
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.personAddFace(new PostParameters().setPersonId(personID).setFaceId(faceID));
            } catch (FaceppParseException e) {
                e.printStackTrace();
            }
            System.out.println("result: " + result.toString() + "\n");
            return result.toString();
        }

          /**
          * Removes a face from a person using the person's ID and the face ID.
          */
        protected String removeFace(String personID, String faceID){
            System.out.println("Removing face from person: " + personID);
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.personRemoveFace(new PostParameters().setPersonId(personID).setFaceId(faceID));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            System.out.println("Remove face result: " + result.toString());
            return result.toString();
        }



          /**
          * Creates a person group.
           * returns the group id
          */
        protected String createGroup(){
            System.out.println("[facepp] Creating group: " + groupName + "\n");
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.groupCreate(new PostParameters().setGroupName(groupName));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            System.out.println("create group result: " + result.toString() + "\n");
            return result.toString();
        }

          /**
          * Removes an existing person group.
          * Since the class uses one group name, shouldn't this take no parameters? Certain functions don't take
           * group name as a parameter and assume it's the one assigned to the class instance.
          */
        protected String removeGroup(String groupName){
        JSONObject result = new JSONObject();
        System.out.println("removing group: " + groupName + "\n");
        try {
            result = httpRequests.groupDelete(new PostParameters().setGroupName(groupName));
        } catch (FaceppParseException e){
            e.printStackTrace();
        }
        System.out.println("result: " + result.toString() + "\n");
        return result.toString();
        }

          /**
          * Adds a person to a group with the person's ID.
          */
          private String addPersonToGroup(String personID){
              System.out.println("Adding person to group: " + personID + "\n");
              JSONObject result = new JSONObject();
              try {
                  result = httpRequests.groupAddPerson(new PostParameters().setGroupName(groupName).setPersonId(personID));
              }
              catch (FaceppParseException e) {
                  e.printStackTrace();
              }
              System.out.println("result: " + result.toString() + "\n");
              return result.toString();
          }


        /**
          * Face trains a person in preparation for identification.
          */
        protected String trainPerson(String personID){
            System.out.println("Training Person" + personID + "\n");
            JSONObject result = new JSONObject();
            try {
                result  = httpRequests.trainVerify(new PostParameters().setPersonId(personID));
                return result.getString("session_id");
            } catch (FaceppParseException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("result: " + result.toString() + "\n");
            return "FAILURE";
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
            sessionInfo = httpRequests.infoGetSession(new PostParameters().setSessionId(result.getString("session_id")));
            System.out.println("Session Info: " + sessionInfo.toString() + "\n");
            return sessionInfo.toString();
        } catch (FaceppParseException e){
            e.printStackTrace();
        } catch (JSONException e) {
           e.printStackTrace();
        }
        return "UNSUCCESFULL RETRIEVAL OF SESSION INFO FOR TRAINING";
    }



        /**
          * Attempts to identify the person in the group in the image at the given URL
          */
        protected String identifyPersonInGroup(String url){
            System.out.println("Identifying person in url: " + url + "\n");
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.recognitionIdentify(new PostParameters().setGroupName(groupName).setUrl(url));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            System.out.println("result: " + result.toString() + "\n");
            return result.toString();
        }

        protected String identifyPerson(String personId, String url){
            System.out.println("Identifying person in url: " + url + "\n");
            JSONObject result = new JSONObject();
            String faceId = detectFace(url);
            try {
                result = httpRequests.recognitionVerify(new PostParameters().setPersonId(personId).setFaceId(faceId));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            System.out.println("result: " + result.toString() + "\n");
            return result.toString();
        }


    /**
        * Attempts to identify the person in the given image file.
        */
        protected String identifyPersonInGroup(File file){
            System.out.println("Identifying person in file: " + file + "\n");
            JSONObject result = new JSONObject();
            try {
                result = httpRequests.trainIdentify(new PostParameters().setGroupName(groupName).setImg(file));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            System.out.println("result: " + result.toString() + "\n");
            return result.toString();
        }

        protected String identifyPerson(String personId, File file){
            System.out.println("Identifying person in url: " + file + "\n");
            JSONObject result = new JSONObject();
            String faceId = detectFace(file);
            try {
                result = httpRequests.recognitionVerify(new PostParameters().setPersonId(personId).setFaceId(faceId));
            } catch (FaceppParseException e){
                e.printStackTrace();
            }
            System.out.println("result: " + result.toString() + "\n");
            return result.toString();
        }
          /**
          * Attempts to identify the person in the given image byte array.
          */
         protected String identifyPersonInGroup(byte[] data){
             System.out.println("Identifying person in data: " + data + "\n");
             JSONObject result = new JSONObject();
             try {
                 result = httpRequests.trainIdentify(new PostParameters().setGroupName(groupName).setImg(data));
             } catch (FaceppParseException e){
                 e.printStackTrace();
             }
             System.out.println("result: " + result.toString() + "\n");
             return result.toString();
         }

        protected JSONObject identifyPerson(String personId, byte[] data){
            System.out.println("Identifying person in url: " + data + "\n");
            JSONObject result = new JSONObject();
            String faceId = detectFace(data);
            try {
                result = httpRequests.recognitionVerify(new PostParameters().setPersonId(personId).setFaceId(faceId));
            } catch (FaceppParseException e){
                e.printStackTrace();
                return null;
            }
            System.out.println("result: " + result.toString() + "\n");
            return result;
        }

    public FRServiceHandlerTrainResponse train(String userID, List<ImageData> images) {
        String serviceName = "facepp";
        FacePPCommunicator fpp = new FacePPCommunicator(apiKey, apiSecret);
        FRServiceHandlerTrainResponse response =
                new FRServiceHandlerTrainResponse(serviceName, false, userID, false, images);
        if (userID == null){
            return response;
        }
        if (images == null) {
            return response;
        }
        String personId = fpp.createPerson(userID);
        Iterator<ImageData> it = images.iterator();
        JSONObject result;
        int tries = 0;
        boolean goodTraining = false;
        boolean serverResponded = false;
        while (it.hasNext()) {
            fpp.addFaceToPerson(personId,it.next().getImageBinary());
        }
        do {
            try {
                String sessionId = fpp.trainPerson(personId);
                serverResponded = true;
                TimeUnit.SECONDS.sleep(1);
                result = httpRequests.infoGetSession(new PostParameters().setSessionId(sessionId));
                goodTraining = (result.getString("status") == "SUCC");
            }
            catch (FaceppParseException e){
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            tries++;
        } while ((!goodTraining) && (tries < 10));
        FRServiceHandlerTrainResponse response2 =
                new FRServiceHandlerTrainResponse(serviceName, serverResponded, personId, goodTraining, images);
        return response2;
    }

    public FRServiceHandlerVerifyResponse verify(String personID, ImageData image){
        String serviceName = "facepp";
        boolean serviceResponded;
        float confidence;
        final float cutoff;
        String FRPersonID = personID;
        JSONObject result = identifyPerson(personID, image.getImageBinary());
        FRServiceHandlerVerifyResponse response = new FRServiceHandlerVerifyResponse(serviceName,false,0,80,personID);
        if (personID == null) {
            return response;
        }
        if (image == null){
            return response;
        }
        if (result != null) {
            {
            try {
                confidence = (float)result.getDouble("confidence");
            }
            catch (JSONException e){
                e.printStackTrace();
                return response;
            }
            serviceResponded = true;
            cutoff = 80;
            FRServiceHandlerVerifyResponse response2 =
                    new FRServiceHandlerVerifyResponse(serviceName,serviceResponded,confidence,cutoff,personID);
                return response2;
            }

            }
        return response;

    }
    public String getFRServiceName(){
        return "facepp";
    }

    public float getFRServiceCutoff(){
        return 80;
    }
    public static void main(String[] args) {
        String apiKey = "15bd4507cc5030eb330d4ab62ce5fd69";
        String apiSecret = "LI5HIlv6RsvY_Si2t0GwE9NQZPGbuHW6";
        FacePPCommunicator fpp = new FacePPCommunicator(apiKey, apiSecret);
        //fpp.removeGroup(fpp.groupName);
        fpp.createGroup();
       // fpp.removePersonWName("person_8");
     //   fpp.removePerson("person_8");
        String personId = fpp.createPersonInGroup("person_8");
        fpp.addFaceToPerson(personId,
                "https://www.whitehouse.gov/sites/whitehouse.gov/files/images/first-family/44_barack_obama%5B1%5D.jpg");

        fpp.addFaceToPerson(personId,
                "http://www.worldnewspolitics.com/wp-content/uploads/2016/02/140718-barack-obama-2115_86aea53294a878936633ec10495866b6.jpg");

        fpp.addFaceToPerson(personId,
                "http://i2.cdn.turner.com/cnnnext/dam/assets/150213095929-27-obama-0213-super-169.jpg");

        fpp.addFaceToPerson(personId,
                "http://a.abcnews.go.com/images/US/AP_obama8_ml_150618_16x9_992.jpg");

        fpp.addFaceToPerson(personId,
                "http://www.dailystormer.com/wp-content/uploads/2015/06/2014-10-12-obama-618x402.jpg");

        fpp.addFaceToPerson(personId,
                "http://media.vocativ.com/photos/2015/10/RTS2O4I-22195838534.jpg");

        fpp.addFaceToPerson(personId,
                "https://upload.wikimedia.org/wikipedia/commons/e/e9/Official_portrait_of_Barack_Obama.jpg");

        fpp.addFaceToPerson(personId,
                "http://cbsnews2.cbsistatic.com/hub/i/r/2015/10/09/f27caaea-86d1-41e2-bec2-97e89e5dba03/thumbnail/770x430/0a4b24d154ee526bb6811f1888e16600/presidentobamamain.jpg");
        fpp.trainPerson(personId);
        try{
        TimeUnit.SECONDS.sleep(5);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        fpp.identifyPerson(personId, "http://i.huffpost.com/gen/2518262/images/n-OBAMA-628x314.jpg");
         fpp.removePersonWName("person_8"); //removing the person removes them from facepp removes them from the group.
       // fpp.removePersonFromGroup(personId);
       fpp.removeGroup(fpp.groupName);
    }
}




