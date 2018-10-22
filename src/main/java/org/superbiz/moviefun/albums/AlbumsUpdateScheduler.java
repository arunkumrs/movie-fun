package org.superbiz.moviefun.albums;

import com.amazonaws.services.dynamodbv2.xspec.S;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;

@Configuration
@EnableAsync
@EnableScheduling
public class AlbumsUpdateScheduler {

    private static final long SECONDS = 1000;
    private static final long MINUTES = 60 * SECONDS;

    private final AlbumsUpdater albumsUpdater;
    private final AlbumsBean albumsBean;
    private final AlbumSchedulerTask albumSchedulerTask = new AlbumSchedulerTask();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AlbumsUpdateScheduler(AlbumsUpdater albumsUpdater, AlbumsBean albumsBean) {
        this.albumsUpdater = albumsUpdater;
        this.albumsBean = albumsBean;
    }


    @Scheduled(initialDelay = 15 * SECONDS, fixedRate = 30 * SECONDS)
    public void run() {
        try {
            if (okToRunTheTask()) {
                logger.debug("Starting albums update" + Thread.currentThread().getName());
                albumsUpdater.update();
                createOrUpdateSchedulerTask(albumSchedulerTask);
                logger.debug("Finished albums update" + Thread.currentThread().getName());
            } else {
                logger.debug("Just ran recently" + Thread.currentThread().getName());
            }

        } catch (Throwable e) {
            logger.error("Error while updating albums", e);
        }
    }

    private void createOrUpdateSchedulerTask(AlbumSchedulerTask albumSchedulerTask) {
        albumSchedulerTask.setStartedAt(new Timestamp(System.currentTimeMillis()));
        albumsBean.updateSchedulerTimeStamp(albumSchedulerTask);
    }

    private boolean okToRunTheTask() {
        AlbumSchedulerTask albumSchedulerTaskDB = albumsBean.getSchedulerTimeStamp();
        if (albumSchedulerTaskDB == null)
            return true;
        //update the scheduler task id with present id in db if null
        if (albumSchedulerTask.getId() == null)
            albumSchedulerTask.setId(albumSchedulerTaskDB.getId());
        long currentTime = System.currentTimeMillis();
        long dbTimeStamp = albumSchedulerTaskDB.getStartedAt().getTime();
        long diffTimeInMill = currentTime - dbTimeStamp;
        long diffTimeInSeconds = diffTimeInMill / 1000;
        return diffTimeInSeconds >= 40 ? true : false;
    }
}
