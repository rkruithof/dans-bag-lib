package nl.knaw.dans.bag.v0

import better.files.File
import nl.knaw.dans.bag.{ FileSystemSupport, TestSupportFixture }

class BagUseCases extends TestSupportFixture with FileSystemSupport {

  private val simpleBagDir: File = testDir / "simple-bag"

  override protected def beforeEach(): Unit = {
    super.beforeEach()

    File(getClass.getResource("/test-bags/simple-bag")).copyTo(simpleBagDir)
  }

  "bag" should "be able to create an empty bag with default algorithm and no bag-info" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.empty(simpleBagDir)
    """.stripMargin should compile
  }

  it should "be able to create an empty bag with multiple algorithm and no bag-info" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.empty(simpleBagDir, Set(ChecksumAlgorithm.MD5, ChecksumAlgorithm.SHA1))
    """.stripMargin should compile
  }

  it should "be able to create an empty bag with default algorithm and custom bag-info" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.empty(simpleBagDir, bagInfo = Map("Some-Value" -> Seq("abcdef")))
    """.stripMargin should compile
  }

  it should "be able to create an empty bag with custom algorithm and bag-info" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.empty(simpleBagDir, Set(ChecksumAlgorithm.SHA256), Map("Some-Value" -> Seq("abcdef")))
    """.stripMargin should compile
  }

  it should "be able to create a bag from data with default algorithm and no bag-info" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.createFromData(simpleBagDir)
    """.stripMargin should compile
  }

  it should "be able to create a bag from data with multiple algorithm and no bag-info" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.createFromData(simpleBagDir, Set(ChecksumAlgorithm.MD5, ChecksumAlgorithm.SHA1))
    """.stripMargin should compile
  }

  it should "be able to create a bag from data with default algorithm and custom bag-info" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.createFromData(simpleBagDir, bagInfo = Map("Some-Value" -> Seq("abcdef")))
    """.stripMargin should compile
  }

  it should "be able to create a bag from data with custom algorithm and bag-info" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.createFromData(simpleBagDir, Set(ChecksumAlgorithm.SHA256), Map("Some-Value" -> Seq("abcdef")))
    """.stripMargin should compile
  }

  it should "be able to read a bag" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir)
    """.stripMargin should compile
  }

  it should "be able to get the bag's base directory" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir)
      |val baseDir: Try[File] = bag.map(_.baseDir)
    """.stripMargin should compile
  }

  it should "be able to get the bag's data directory" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir)
      |val baseDir: Try[File] = bag.map(_.data)
    """.stripMargin should compile
  }

  it should "be able to get the BagIt version" in {
    """
      |import gov.loc.repository.bagit.domain.Version
      |import scala.util.Try
      |
      |val version: Try[Version] = Bag.read(simpleBagDir).map(_.bagitVersion)
    """.stripMargin should compile
  }

  it should "be able to set the BagIt version number" in {
    """
      |import gov.loc.repository.bagit.domain.Version
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.withBagitVersion(new Version(0, 97)))
    """.stripMargin should compile
  }

  it should "be able to set the BagIt version number without creating a Version yourself" in {
    """
      |import gov.loc.repository.bagit.domain.Version
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.withBagitVersion(0, 97))
    """.stripMargin should compile
  }

  it should "be able to get the file encoding" in {
    """
      |import java.nio.charset.Charset
      |import scala.util.Try
      |
      |val version: Try[Charset] = Bag.read(simpleBagDir).map(_.fileEncoding)
    """.stripMargin should compile
  }

  it should "be able to set the file encoding" in {
    """
      |import java.nio.charset.StandardCharsets
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.withFileEncoding(StandardCharsets.UTF_8))
    """.stripMargin should compile
  }

  it should "be able to list all entries from bag-info.txt" in {
    """
      |import scala.util.Try
      |
      |val bagInfo: Try[Map[String, Seq[String]]] = Bag.read(simpleBagDir).map(_.bagInfo)
    """.stripMargin should compile
  }

  it should "be able to add an entry to bag-info.txt" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.addBagInfo("some-key", "some-value"))
    """.stripMargin should compile
  }

  it should "be able to remove an entry from bag-info.txt" in {
    """
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.removeBagInfo("some-key"))
    """.stripMargin should compile
  }

  it should "be able to get the 'Created' keyword from bag-info.txt" in {
    """
      |import scala.util.Try
      |import org.joda.time.DateTime
      |
      |val bag: Try[Option[DateTime]] = Bag.read(simpleBagDir).flatMap(_.created)
    """.stripMargin should compile
  }

  it should "be able to add/update the 'Created' keyword in bag-info.txt (default argument = now)" in {
    """
      |import scala.util.Try
      |import org.joda.time.DateTime
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.withCreated())
    """.stripMargin should compile
  }

  it should "be able to add/update the 'Created' keyword in bag-info.txt (custom argument)" in {
    """
      |import scala.util.Try
      |import org.joda.time.DateTime
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.withCreated(DateTime.now()))
    """.stripMargin should compile
  }

  it should "be able to remove the 'Created' keyword from bag-info.txt" in {
    """
      |import scala.util.Try
      |import org.joda.time.DateTime
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.withoutCreated())
    """.stripMargin should compile
  }

  it should "be able to get the 'Is-Version-Of' keyword from bag-info.txt" in {
    """
      |import scala.util.Try
      |import java.net.URI
      |
      |val bag: Try[Option[URI]] = Bag.read(simpleBagDir).flatMap(_.isVersionOf)
    """.stripMargin should compile
  }

  it should "be able to add/update the 'Is-Version-Of' keyword in bag-info.txt" in {
    """
      |import scala.util.Try
      |import java.util.UUID
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.withIsVersionOf(UUID.randomUUID()))
    """.stripMargin should compile
  }

  it should "be able to remove the 'Is-Version-Of' keyword from bag-info.txt" in {
    """
      |import scala.util.Try
      |import org.joda.time.DateTime
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir).map(_.withoutIsVersionOf())
    """.stripMargin should compile
  }

  it should "be able to list all entries from fetch.txt" in {
    """
      |import nl.knaw.dans.bag.v0.FetchItem
      |import scala.util.Try
      |
      |val fetchItems: Try[Seq[FetchItem]] = Bag.read(simpleBagDir).map(_.fetchFiles)
    """.stripMargin should compile
  }

  it should "be able to add an entry to fetch.txt" in {
    """
      |import java.net.URL
      |import nl.knaw.dans.bag.v0.FetchItem
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.addFetchFile(new URL("http://x"), 12L, _ / "fetched-file.txt"))
    """.stripMargin should compile
  }

  it should "be able to remove an entry from fetch.txt by file name/path" in {
    """
      |import nl.knaw.dans.bag.v0.FetchItem
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.removeFetchByFile(_ / "fetched-file.txt"))
    """.stripMargin should compile
  }

  it should "be able to remove an entry from fetch.txt by url" in {
    """
      |import java.net.URL
      |import nl.knaw.dans.bag.v0.FetchItem
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.removeFetchByURL(new URL("http://x")))
    """.stripMargin should compile
  }

  it should "be able to remove an entry from fetch.txt" in {
    """
      |import java.net.URL
      |import nl.knaw.dans.bag.v0.FetchItem
      |import scala.util.Try
      |
      |val bag: Try[Bag] = Bag.read(simpleBagDir)
      |    .map(bag => bag.removeFetch(FetchItem(new URL("http://x"), 12L, bag.data / "fetched-file.txt")))
    """.stripMargin should compile
  }

  it should "be able to list the algorithms used to checksum the payload files" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val algorithms: Try[Set[ChecksumAlgorithm]] = Bag.read(simpleBagDir)
      |    .map(_.payloadManifestAlgorithms)
    """.stripMargin should compile
  }

  it should "be able to add an algorithm used to checksum all payload files in the bag" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val manifests: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.addPayloadManifestAlgorithm(ChecksumAlgorithm.SHA512))
    """.stripMargin should compile
  }

  it should "be able to update the checksums for an existing payload algorithm" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val manifests: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.addPayloadManifestAlgorithm(ChecksumAlgorithm.SHA512, updateManifest = true))
    """.stripMargin should compile
  }

  it should "be able to remove an algorithm used to checksum all payload files in the bag" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val manifests: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.removePayloadManifestAlgorithm(ChecksumAlgorithm.SHA1))
    """.stripMargin should compile
  }

  it should "be able to list the algorithms for the tag files" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val algorithms: Try[Set[ChecksumAlgorithm]] = Bag.read(simpleBagDir)
      |    .map(_.tagManifestAlgorithms)
    """.stripMargin should compile
  }

  it should "be able to add an algorithm used to checksum all tag files in the bag" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val manifests: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.addTagManifestAlgorithm(ChecksumAlgorithm.SHA512))
    """.stripMargin should compile
  }

  it should "be able to update the checksums for an existing tag algorithm" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val manifests: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.addTagManifestAlgorithm(ChecksumAlgorithm.SHA512, updateManifest = true))
    """.stripMargin should compile
  }

  it should "be able to remove an algorithm used to checksum all tag files in the bag" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val manifests: Try[Bag] = Bag.read(simpleBagDir)
      |    .flatMap(bag => bag.removeTagManifestAlgorithm(ChecksumAlgorithm.SHA512))
    """.stripMargin should compile
  }

  it should "be able to list all payload files in the bag, with their checksums, for each algorithm" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val manifests: Try[Map[ChecksumAlgorithm, Map[File, String]]] = Bag.read(simpleBagDir)
      |    .map(_.payloadManifests)
    """.stripMargin should compile
  }

  it should "be able to add a payload file to a bag, originating from an InputStream" in {
    """
      |import java.io.InputStream
      |import scala.util.Try
      |
      |val inputStream: InputStream = ???
      |val resultBag: Try[Bag] = Bag.read(simpleBagDir)
      |  .flatMap(_.addPayloadFile(inputStream)(_ / "test.txt"))
    """.stripMargin should compile
  }

  it should "be able to add a payload file to a bag, originating from another file" in {
    """
      |import scala.util.Try
      |
      |val input: File = ???
      |val resultBag: Try[Bag] = Bag.read(simpleBagDir)
      |  .flatMap(_.addPayloadFile(input)(_ / "test.txt"))
    """.stripMargin should compile
  }

  it should "be able to remove a payload file from the bag" in {
    """
      |import scala.util.Try
      |
      |val resultBag: Try[Bag] = Bag.read(simpleBagDir)
      |  .flatMap(_.removePayloadFile(_ / "test.txt"))
    """.stripMargin should compile
  }

  it should "be able to list all tag files in the bag, with their checksums, for each algorithm" in {
    """
      |import nl.knaw.dans.bag.v0.ChecksumAlgorithm.ChecksumAlgorithm
      |import scala.util.Try
      |
      |val manifests: Try[Map[ChecksumAlgorithm, Map[File, String]]] = Bag.read(simpleBagDir)
      |    .map(_.tagManifests)
    """.stripMargin should compile
  }

  it should "be able to add a tag file to a bag, originating from an InputStream" in {
    """
      |import java.io.InputStream
      |import scala.util.Try
      |
      |val inputStream: InputStream = ???
      |val resultBag: Try[Bag] = Bag.read(simpleBagDir)
      |  .flatMap(_.addTagFile(inputStream)(_ / "test.txt"))
    """.stripMargin should compile
  }

  it should "be able to add a tag file to a bag, originating from another file" in {
    """
      |import scala.util.Try
      |
      |val input: File = ???
      |val resultBag: Try[Bag] = Bag.read(simpleBagDir)
      |  .flatMap(_.addTagFile(input)(_ / "test.txt"))
    """.stripMargin should compile
  }

  it should "be able to remove a tag file from the bag" in {
    """
      |import scala.util.Try
      |
      |val resultBag: Try[Bag] = Bag.read(simpleBagDir)
      |  .flatMap(_.removeTagFile(_ / "test.txt"))
    """.stripMargin should compile
  }

  it should "be able to save changes made to the bag directory on the file system" in {
    """
      |import scala.util.Try
      |
      |val saved: Try[Unit] = Bag.read(simpleBagDir)
      |    // .flatMap(_.some_modifications)
      |    .flatMap(_.save)
    """.stripMargin should compile
  }
}