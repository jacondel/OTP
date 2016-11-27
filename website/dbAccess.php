<?php

//$t = $_GET["type"];
$t = $_REQUEST["type"];




function randomString($length) {
    $str = "";
    $characters = array_merge(range('A','Z'), range('a','z'), range('0','9'));
    $max = count($characters) - 1;
    for ($i = 0; $i < $length; $i++) {
        $rand = mt_rand(0, $max);
        $str .= $characters[$rand];
    }
    return $str;
}

function myHash($input){
    return substr(hash('sha256',$input),0,4);
}

function applyHashN($seed,$length) {

    $str = $seed;
    for ($i = 0; $i < $length; $i++) {
        $str = myHash($str);
    }
    return $str;
}

function updateHash($userName,$hash){
    $servername = "localhost";
    $username = "root";
    $password = "root";
    $dbname = "OTP"; //db example table users
    $conn = mysqli_connect($servername, $username, $password, $dbname);
    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = mysqli_stmt_init($conn);
    if (mysqli_stmt_prepare($sql,"INSERT INTO Users VALUES (?,?) ON DUPLICATE KEY UPDATE hash = ?" )){
        mysqli_stmt_bind_param($sql,"sss",$userName,$hash,$hash);
        if(mysqli_execute($sql)){
            return true;
        }
        mysqli_stmt_close($sql);
    }
    
    mysqli_close($conn);
}

function validateHash($userName,$hash){
    $servername = "localhost";
    $username = "root";
    $password = "root";
    $dbname = "OTP"; //db example table users
    $conn = mysqli_connect($servername, $username, $password, $dbname);
    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    $sql = mysqli_stmt_init($conn);
    if (mysqli_stmt_prepare($sql,"SELECT * FROM Users WHERE Users.username=?" )){
        mysqli_stmt_bind_param($sql,"s",$userName);
        if(mysqli_execute($sql)){
            $result = mysqli_stmt_get_result($sql);
            $row = mysqli_fetch_array($result);
            $expected = myHash($hash);
             if ( $expected == $row["hash"]) {
                if(updateHash($userName,$hash)){
                    echo "Success";
                }
             }else{
                //echo $row["hash"];
                echo "Failure";
             }
        }
        mysqli_stmt_close($sql);
    }
    
    mysqli_close($conn);
}

if($t == "addUser" &&  isSet($_REQUEST["username"]) ){
    // Create connection
    $userName = $_REQUEST["username"];
    $seed = randomString(mt_rand(5,10));
    $count = mt_rand(500,1000);
    $hash = applyHashN($seed,$count);
    if (updateHash($userName,$hash)){
        echo "seed: " . $seed . "</br>" . "count: " . ($count-1);
    }

}

if($t == "validateUser" &&  isSet($_REQUEST["username"]) &&  isSet($_REQUEST["hash"]) ){
    // Create connection
    $userName = $_REQUEST["username"];
    $hash = $_REQUEST["hash"];
    validateHash($userName,$hash);
    

}

?>