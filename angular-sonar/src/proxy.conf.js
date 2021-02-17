const PROXY_CONFIG = [
	{
			context: [
					"/sonarqube/api/metrics",
					"/sonarqube/api/server/version",
					"/sonarqube/api/authentication/login"
			],
			target: "http://localhost:9000",
			secure: false,
			logLevel: "debug",
	}
]

module.exports = PROXY_CONFIG;
