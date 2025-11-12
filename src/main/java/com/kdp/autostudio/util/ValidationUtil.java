package com.kdp.autostudio.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for validating KDP book files (pre-flight checks).
 */
public class ValidationUtil {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    public static class ValidationResult {
        private boolean passed;
        private List<String> errors;
        private List<String> warnings;

        public ValidationResult() {
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
            this.passed = true;
        }

        public void addError(String error) {
            this.errors.add(error);
            this.passed = false;
        }

        public void addWarning(String warning) {
            this.warnings.add(warning);
        }

        public boolean isPassed() {
            return passed;
        }

        public List<String> getErrors() {
            return errors;
        }

        public List<String> getWarnings() {
            return warnings;
        }
    }

    /**
     * Validate a project's files for KDP compliance.
     */
    public static ValidationResult validateProject(String projectPath) {
        ValidationResult result = new ValidationResult();

        File projectDir = new File(projectPath);
        if (!projectDir.exists()) {
            result.addError("Project directory does not exist: " + projectPath);
            return result;
        }

        // Check for required files
        File interiorPdf = new File(projectPath + File.separator + "interior" + File.separator + "interior.pdf");
        File coverPdf = new File(projectPath + File.separator + "cover" + File.separator + "cover.pdf");

        if (!interiorPdf.exists()) {
            result.addError("Interior PDF not found");
        }

        if (!coverPdf.exists()) {
            result.addError("Cover PDF not found");
        }

        // TODO: Add actual PDF validation using PDF library
        // - Check trim size (6x9, 8.5x11, etc.)
        // - Check bleed (0.125 inches)
        // - Check page count
        // - Check spine width calculation
        // - Check font embedding
        // - Check color space (CMYK for covers)
        // - Check DPI (300 minimum)

        if (result.isPassed()) {
            result.addWarning("Full PDF validation requires Ghostscript/ImageMagick integration");
        }

        return result;
    }

    /**
     * Validate metadata for KDP compliance.
     */
    public static ValidationResult validateMetadata(String metadataJson) {
        ValidationResult result = new ValidationResult();

        // TODO: Parse JSON and validate
        // - Title length (max 200 chars)
        // - Subtitle length (max 200 chars)
        // - Keywords count (exactly 7)
        // - Description length (500-2000 chars)
        // - BISAC categories (valid codes)
        // - No trademarked keywords
        // - No unsafe content

        return result;
    }
}

