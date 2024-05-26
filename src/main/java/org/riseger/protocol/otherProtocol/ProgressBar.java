package org.riseger.protocol.otherProtocol;

public interface ProgressBar {
    void loading(int currentStep);

    void done();
}
