package co.becuz.services;

import java.util.Collection;

import co.becuz.domain.Frame;

public interface FrameService {
    Frame getFrameById(String id);

    Collection<Frame> getAllFrames();

	void delete(String id);

	Frame save(Frame frame);

	Frame update(Frame frame);
}
