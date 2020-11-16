import React, {useState,useEffect,useCallback} from "react";
import logo from './logo.svg';
import './App.css';
import axios from "axios";
import {useDropzone} from 'react-dropzone'


const UserProfiles = () =>{
  const [userProfiles,setUserProfiles] = useState([]);
   const fetchUserProfiles = ()=>{
     axios.get("http://localhost:8080/api/v1/user-profile/").then(res => {
      console.log("Helloooo============"+res);
      setUserProfiles(res.data);
     });
   }

   useEffect (()=> {
    fetchUserProfiles();
  },[]);
 
  return userProfiles.map((userProfile,index) => {
    return (
      <div key={index}>

        {
        userProfile.userProfileId ? <img src={`http://localhost:8080/api/v1/user-profile/${userProfile.userProfileId}/image/download`}/> : null}
        <br/>
        <br/>
        <h1>{userProfile.username}</h1>
        <h1>{userProfile.userProfileId}</h1>
        <Dropzone {...userProfile}/>
        {/* it is same as userProfileId = {userProfile.userProfileId} */}
        <br/>
        </div>
    )
  }

  )
};

function Dropzone({userProfileId}) {
  const onDrop = useCallback(acceptedFiles => {
    const file = acceptedFiles[0];
    console.log(file);
    console.log("userProfileId==========="+userProfileId);
   
    const formData = new FormData();
    formData.append("file",file);
    let url = `http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`;

    axios.post(url,
     formData,
     {
       headers:{
         "content-type": "multipart/form-data"
       }
     }
     ).then(() => {
      console.log("File Uploaded Successfully");
     }).catch(err=>{
       console.log(err);
     })
  }, [])
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the image here ...</p> :
          <p>Drag 'n' drop Profile Image, or click to select files</p>
      }
    </div>
  )
}




function App() {
  return (
    <div className="App">  
     <UserProfiles />

    </div>
  );
}

export default App;
