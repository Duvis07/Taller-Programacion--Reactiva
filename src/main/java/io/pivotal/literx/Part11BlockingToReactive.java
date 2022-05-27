package io.pivotal.literx;

import io.pivotal.literx.domain.User;
import io.pivotal.literx.repository.BlockingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author Duvan Botero
 * @version  1.2
 * @email duvanarleybotero@gmail.com
 */
public class Part11BlockingToReactive {

//========================================================================================

    // TODO Create a Flux for reading all users from the blocking repository deferred until the flux is subscribed, and run it with an elastic scheduler
    Flux<User> blockingRepositoryToFlux(BlockingRepository<User> repository) {
        return Flux.defer(() -> Flux.fromIterable(repository.findAll()))
                .subscribeOn(Schedulers.elastic());

    }

//========================================================================================

    // TODO Insert users contained in the Flux parameter in the blocking repository using an elastic scheduler and return a Mono<Void> that signal the end of the operation
    Mono<Void> fluxToBlockingRepository(Flux<User> flux, BlockingRepository<User> repository) {

        return flux
                .publishOn(Schedulers.elastic())
                .flatMap(user -> {
                    repository.save(user);
                    return Mono.empty();
                })
                .then();
    }


}
