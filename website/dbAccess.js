function addUserToDBSuccess(name,callback){
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      callback.apply(this);
    }
  };
  xhttp.open("GET", "dbAccess.php?type=addUser&username="+name);
  xhttp.send();
}

//-------------------------------------------------------

function validateUserFromDBSuccess(name,hash,callback){
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      callback.apply(this);
    }
  };
  xhttp.open("GET", "dbAccess.php?type=validateUser&username="+name+"&hash="+hash);
  xhttp.send();
}

//-------------------------------------------------------