<?php
	require 'connection.php';
	$query_string = "";
	if($_POST){
		$kv = array();
		foreach($_POST as $key=>$value){
			$kv[$key] = "$value";			
		}
		//$query_string = join("&", $kv);
		//echo $query_string;
		echo $kv['pgname'];
		echo $kv['rating'];
		
		mysqli_query($connect,"update pgsetup set pgrating='$kv[rating]' where pgname='$kv[pgname]'") or die("Not updated!");
        $action_performed ="Rating Updated";
		echo $action_performed;
		
	}else{
		echo 'No response!';
	}
?>