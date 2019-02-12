import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable

class ObsSpec extends FlatSpec with Matchers {

  "registerSubscribers" should "successfully register valid subscriber" in {
    val obs = new Obs[Int](mutable.MutableList())
    obs.subscribers.size shouldBe 0

    obs.registerSubscribers(i => println(i))
    obs.subscribers.size shouldBe 1
  }

  //TODO: How should it handle invalid suscribers?

  "observe" should "notify each subscriber of value on stream" in {
    val obs = new Obs[Int](mutable.MutableList())
    obs.registerSubscribers(i => println(i))
    obs.registerSubscribers(i => println(i.toString.length))

    obs.observe(2)

    //I see the printout but how to test
  }

  "map" should "return a new Obs of new type T2" in {
    val obs = new Obs[Int](mutable.MutableList())
    obs.registerSubscribers(i => println(i))

    val obs2 = obs.map(i => i*2)

    obs2.registerSubscribers(i => println(i))

    obs.observe(2)
    //I see the printout but how to test
  }

  it should "handle completely new types" in {
    val obs = new Obs[String](mutable.MutableList())
    obs.registerSubscribers(i => println(i))

    val obs2 = obs.map(i => i.length)
    obs2.registerSubscribers(i => println(i))
    obs.observe("length")

    //I see the printout but how to test
  }

  "scan" should "return a new Obs that continually reduces" in {
    val obs = new Obs[Int](mutable.MutableList())
    obs.registerSubscribers(i => println(i))

    val obs2 = obs.scan(0)((a,b) => a+b)
    obs2.registerSubscribers(i => println(i))

    obs.observe(2)
    obs.observe(4)
    obs.observe(6)
    obs.observe(8)

    //I see the printout but how to test
  }

  "zip" should "return a new Obs that interweaves two Obs" in {
    val obs = new Obs[String](mutable.MutableList())
    val obs2 = new Obs[Int](mutable.MutableList())
    val obs3 = obs.zip(obs2)

    obs.registerSubscribers(i => println(i))
    obs2.registerSubscribers(i => println(i))
    obs3.registerSubscribers(i => println(i))

    obs.observe("test")
    obs2.observe(3)

    //I see the printout but how to test
  }
}
