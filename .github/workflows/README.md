name: Commit Stage
on: push

jobs:
  build:
    name: Build and Test
    runs-on: ubuntu-22.04
    permissions:
      contents: read
      security-events: write
    steps:
      # Checks out the current Git repository
      - name: Checkout source code
        uses: actions/checkout@v3
      # Install and configure a Java runtime
      - name: Set up JDK
        uses: actions/setup-java@v3
        # Define which version, distribution, and cache type to use
        with:
          distribution: temurin
          java-version: 17
          cache: maven
      - name: Build, unit tests and integration tests
        run: |
          # Ensure the Maven wrapper is executable, solving Windows incompatibilities
          chmod +x mvnw
          # Run the Maven build task, which compiles the codebase and runs unit and integration tests
          ./mvnw clean install
      # Scan the codebase for vulnerabilities using grype
      - name: Code vulnerability scanning
        uses: anchore/scan-action@v3
        # Assign an identifier to the current step so that it can be referenced from subsequent steps
        id: scan
        with:
          # path to the checked-out repository
          path: "${{ github.workspace }}"
          # Whether to fail the build in the event of security vulnerabilities
          fail-build: false
          # The minimum security category to be considered as an error (low, medium, high, critical)
          severity-cutoff: high
          # Whether to enable the generation of a report after the scan is completed
          acs-report-enable: true
      - name: Upload vulnerability report
        # Upload the security vulnerability report to GitHub (SARIF format)
        uses: github/codeql-action/upload-sarif@v2
        # Upload the report even if the previous step fails
        if: success() || failure()
        with:
          # Fetch the report from the output of the previous step
          sarif_file: ${{ steps.scan.outputs.sarif }}
