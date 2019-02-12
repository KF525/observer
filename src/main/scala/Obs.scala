import scala.collection.mutable
import scala.collection.mutable.MutableList

class Obs[T](val subscribers: MutableList[T => Unit]) {

  def registerSubscribers(sub: T => Unit)= subscribers += sub

  def observe(t: T) = subscribers.foreach(s => s(t))

  def map[T2](f: T => T2): Obs[T2] = {
    val o2 = new Obs[T2](mutable.MutableList())
    this.registerSubscribers(s => o2.observe(f(s)))
    o2
  }

  def scan[T2](s: T2)(f: (T, T2) => T2) = {
    var start = s
    val o2 = new Obs[T2](mutable.MutableList())
    this.registerSubscribers{ s =>
      var next = f(s, start)
      start = next
      o2.observe(next)
    }
    o2
  }

  def zip[T2](that: Obs[T2]): Obs[(T, T2)] = {
    val o3 = new Obs[(T, T2)](mutable.MutableList())
    this.registerSubscribers { s1 =>
      that.registerSubscribers { s2 =>
        o3.observe((s1, s2))
      }
    }
    o3
  }
}
