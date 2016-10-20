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
	$r = mysqli_query($connect,"select * from users where email='$kv[email]'");
	$row=mysqli_fetch_array($r);
	$data[]= $row;
	print json_encode($data);
		
	}else{
		echo 'No response!';
	}
?>