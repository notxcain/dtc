package dtc.tests

import java.time.{Duration, LocalDateTime, ZoneOffset}

import com.fortysevendeg.scalacheck.datetime.jdk8.ArbitraryJdk8._
import dtc.instances.localDateTime._
import dtc.laws.{DateTimeTCTests, LocalDateTimeTCTests, OrderLaws}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Cogen}

class LocalDateTimeTests extends DTCSuiteJVM {

  implicit val arbT: Arbitrary[LocalDateTime] = Arbitrary(genZonedDateTime.map(_.toLocalDateTime))
  implicit val cogenT: Cogen[LocalDateTime] = Cogen(_.toEpochSecond(ZoneOffset.UTC))

  val overflowSafePairGen = for {
    dt <- arbitrary[LocalDateTime]
    dur <- arbitrary[Duration]
  } yield (dt, dur)

  val ldtTests = LocalDateTimeTCTests[LocalDateTime](overflowSafePairGen, genYear)
  checkAll("java.time.LocalDateTime", DateTimeTCTests[LocalDateTime].dateTime)
  checkAll("java.time.LocalDateTime", ldtTests.localDateTime)
  checkAll("java.time.LocalDateTime", ldtTests.monthUntilFractionHandling)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].order)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].partialOrder)
  checkAll("java.time.LocalDateTime", OrderLaws[LocalDateTime].eqv)
}

