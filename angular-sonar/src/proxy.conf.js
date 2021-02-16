const PROXY_CONFIG = [
	{
			context: [
					"/pocs/sonar1/api/metrics",
					"/pocs/sonar1/api/server/version",
					"/pocs/sonar1/api/authentication/login"
			],
			target: "http://localhost:9000",
			secure: false,
			pathRewrite: {
				"/pocs/sonar1/": "/"
			},
			logLevel: "debug",
	}
]

module.exports = PROXY_CONFIG;
