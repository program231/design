package com.migration.platform.component;

import java.util.List;
import java.util.Map;

public interface Verifier extends MigrationComponent {

    VerifyReport verify(Map<String, String> params) throws Exception;

    class VerifyReport {
        private long compared;
        private long diffs;
        private List<String> samples;

        public long getCompared() {
            return compared;
        }

        public void setCompared(long compared) {
            this.compared = compared;
        }

        public long getDiffs() {
            return diffs;
        }

        public void setDiffs(long diffs) {
            this.diffs = diffs;
        }

        public List<String> getSamples() {
            return samples;
        }

        public void setSamples(List<String> samples) {
            this.samples = samples;
        }
    }
}
