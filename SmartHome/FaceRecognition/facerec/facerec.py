import face_recognition
import cv2
import time
from picamera.array import PiRGBArray
from picamera import PiCamera
import glob

#init variables
path_image_folder = "/home/pi/faces"
saveImage = False
save_image_folder = "/home/pi/faces_output"
scale = 8


# initialize the camera and grab a reference to the raw camera capture
camera = PiCamera()
camera.resolution = (1280, 960)
camera.framerate = 32
rawCapture = PiRGBArray(camera, size=(1280, 960))
time.sleep(1)

image_name_list = []
image_list = []
for filename in glob.glob(path_image_folder+'/*.jpg'):
    image_name_list.append(filename)
    tmp = face_recognition.load_image_file(filename)
    image_face_encoding_list.append(face_recognition.face_encodings(tmp)[0])

# Load a sample picture and learn how to recognize it.
#dominik_image = face_recognition.load_image_file("/home/pi/face_recognition/examples/test1.jpg")
#dominik_image2 = face_recognition.load_image_file("output.jpg")
#biden_image = face_recognition.load_image_file("/home/pi/face_recognition/examples/biden.jpg")
#obama_image = face_recognition.load_image_file("/home/pi/face_recognition/examples/obama.jpg")
#dominik_face_encoding = face_recognition.face_encodings(dominik_image)[0]
#dominik_face_encoding2 = face_recognition.face_encodings(dominik_image2)[0]
#biden_face_encoding = face_recognition.face_encodings(biden_image)[0]
#obama_face_encoding = face_recognition.face_encodings(obama_image)[0]

# Initialize some variables
face_locations = []
face_encodings = []
face_names = []
process_this_frame = 0


for frame in camera.capture_continuous(rawCapture, format="bgr", use_video_port=True):
    process_this_frame = process_this_frame + 1
    # Grab a single frame of video
    frame = frame.array
    
    # Resize frame of video to 1/4 size for faster face recognition processing
    small_frame = cv2.resize(frame, (0, 0), fx=(1/scale), fy=(1/scale))

    # Only process every other frame of video to save time
    if process_this_frame==5:
        process_this_frame=0
        # Find all the faces and face encodings in the current frame of video
        face_locations = face_recognition.face_locations(small_frame)
        face_encodings = face_recognition.face_encodings(small_frame, face_locations)

        face_names = []
        for face_encoding in face_encodings:
            # See if the face is a match for the known face(s)
            match = face_recognition.compare_faces(image_face_encoding_list, face_encoding)
            name = "unknown"
            
            for idx,m in enumerate(match):
                if m:
                    name = image_name_list[idx]
            print(name)
            face_names.append(name)

        time.sleep(1)

    


    # Display the results
    for (top, right, bottom, left), name in zip(face_locations, face_names):
        # Scale back up face locations since the frame we detected in was scaled to 1/4 size
        top *= scale
        right *= scale
        bottom *= scale
        left *= scale

        # Draw a box around the face
        cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)

        # Draw a label with a name below the face
        cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (0, 0, 255), cv2.FILLED)
        font = cv2.FONT_HERSHEY_DUPLEX
        cv2.putText(frame, name, (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

    # Display the resulting image
    #cv2.imshow('Video', frame)

    # Save th image and pause
    if saveImage:
        cv2.imwrite("/home/pi/faces_output/faces.jpg",frame)

    rawCapture.truncate(0)
    # Hit 'q' on the keyboard to quit!
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break



# Release handle to the webcam
video_capture.release()
cv2.destroyAllWindows()
