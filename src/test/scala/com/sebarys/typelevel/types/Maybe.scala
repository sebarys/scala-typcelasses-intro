package com.sebarys.typelevel.types

// 1st order kinded type - when we provide concrete type for A we will receive concrete type as a result
sealed trait Maybe[A]

class Just[A](val a: A) extends Maybe[A]
//I don't want to go into variance/covariance/contravariance so just make it a class not object
class Empty[A]() extends Maybe[A]
