<?php
$host = 'localhost';
$username = 'root';
$password = '';
$database = 'pgproject';

try{
	$connect = mysqli_connect($host, $username, $password, $database);
}
catch(Exception $e){
	echo $e->getMessage();
}
?>