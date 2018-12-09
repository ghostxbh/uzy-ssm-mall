package cn.jkj521.bookstore.util;

public class AlipayConfig {

	public static String app_id = "2016091400506865";

	public static String merchant_private_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgjzbVog74fZ5FpqsG+1EVdfV1OkHr/CvSVaTPJ0FNAA/26iJmigRf+gm78GixBqkUJM2jpVA1I2F1uhfil1f2dPaz794H6gJteiFOQVQxEHaTUOVYSTBcBNQL1KscsG6ZDS67xGfSwTBX2Otde7GhI6NWPALgi8qHImPgRRhqIjyC2Q4UKo8xY+iApWwRb7v8ASXhB/R7qQPB8LzgGdjhvIlbXBcyPrlRYQo6JiL0bdl+DX7kulV2D8kKRaYnvNrG1tv1UpmIuTPZOj+oxfeQoa3LW+gIdUjUW6mZUzulqOO/TQfp8s31GaZqipwvdcsOqjHpIWW6aj0i6m4FTz1EwIDAQAB";

	public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqu00Yjdw5/5N7NtSay8TuS2X6pCehbgqKQJtBs5o1Bkttn0xU6iAzWR5QFq9t8hd1zaUKR304Rv0yISLVunchtSem8hktvwAZcenbzdfFhY7apMmcPBM+jpyNuO7zm9bdj5PSVxBe7z2qpXXNZVhrTTEUXQyT3Y1LGRKAtEL42vT80QORaE59quqbtcWi8vHr3mu0hl3CgK0YOhBEiNKyHipD9F9rLOgxycV9/BZlzheK8iKKl1DAUy0vwzLZmqazx7BlC/hJlXwfDUYF3XS9SFRKxqBvYPzl8L37PyRWQBdWtVveY8Vuv0tm7L3qXULCMFIqII4+FYMPutM9AFz2wIDAQAB";

	public static String notify_url = "http://localhost:8080/alipay/alipayNotifyNotice.action";

	public static String return_url = "http://localhost:8080/alipay/alipayReturnNotice.action";

	public static String sign_type = "RSA2";

	public static String charset = "utf-8";

	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
}
