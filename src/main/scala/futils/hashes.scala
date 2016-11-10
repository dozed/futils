package futils

import java.security.MessageDigest

object hashes {
  def sha256(s: String): String = {
    MessageDigest.getInstance("SHA-256").digest(s.getBytes)
      .foldLeft("")((s: String, b: Byte) => s +
        Character.forDigit((b & 0xf0) >> 4, 16) +
        Character.forDigit(b & 0x0f, 16))
  }

  def md5(s: String): String = {
    MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"))
      .foldLeft("")((s: String, b: Byte) => s +
        Character.forDigit((b & 0xf0) >> 4, 16) +
        Character.forDigit(b & 0x0f, 16))
  }
}
