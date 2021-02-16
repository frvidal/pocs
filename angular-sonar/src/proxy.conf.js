const PROXY_CONFIG = [
	{
			context: [
					"/sonar1/api/metrics",
					"/sonar1/api/server/version",
					"/sonar1/api/authentication/login"
			],
			target: "http://localhost:9000",
			secure: false,
			pathRewrite: {
				"/sonar1/": "/"
			},
			logLevel: "debug",
	}
]

module.exports = PROXY_CONFIG;
