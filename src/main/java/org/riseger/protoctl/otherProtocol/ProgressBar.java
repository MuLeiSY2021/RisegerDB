package org.riseger.protoctl.otherProtocol;

public interface ProgressBar {
    void loading(int currentStep);

    void done();
}
