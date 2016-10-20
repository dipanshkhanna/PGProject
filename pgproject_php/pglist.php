<?php
require 'connection.php';
	$r = mysqli_query($connect,"select * from pgsetup");
	while($row=mysqli_fetch_array($r)){
	$data[]= $row;
	}
	print json_encode($data);
?>