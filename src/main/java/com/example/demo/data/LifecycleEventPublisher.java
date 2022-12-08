package com.example.demo.data;

import com.example.demo.data.core.AggregateRoot;
import com.example.demo.data.event.LifecycleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LifecycleEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Pointcut("execution(public * org.springframework.data.repository.Repository+.save*(..) )")
    public void saveMethods() {
    }

    @Pointcut("execution(public * org.springframework.data.repository.Repository+.delete*(..) )")
    public void deleteMethods() {
    }

    @Pointcut("saveMethods() && deleteMethods()")
    public void aggregateLifecycleMethods() {
    }


    @Around("execution(public * org.springframework.data.repository.Repository+.save*(..) )")
    public Object on(ProceedingJoinPoint pjp) throws Throwable {
        Object target = pjp.proceed();
        Set<? extends AggregateRoot> aggregates = extract(pjp.getArgs());
        aggregates.forEach(root -> {
            applicationEventPublisher.publishEvent(LifecycleEvent.from(root));
            log.info("{} --> Updated: {}", root.getClass().getSimpleName(), root.getId());
        });

        return target;

    }

    @Around("deleteMethods()")
    public Object onDelete(ProceedingJoinPoint pjp) throws Throwable {
        Object target = pjp.proceed();
        Set<? extends AggregateRoot> aggregates = extract(pjp.getArgs());
        aggregates.forEach(aggregate -> {
            applicationEventPublisher.publishEvent(LifecycleEvent.deleted(aggregate));
        });
        return target;
    }

    private Set<? extends AggregateRoot> extract(Object[] objects) {
        Set<AggregateRoot> aggregates = new HashSet<>(0);
        Stream.of(objects)
                .forEach(o -> {
                    if (ClassUtils.isAssignable(Collection.class, o.getClass())) {
                        Collection c = (Collection) o;
                        c.stream()
                                .filter(a -> ClassUtils.isAssignable(AggregateRoot.class, a.getClass()))
                                .forEach(a -> aggregates.add((AggregateRoot) a));
                    } else if (ClassUtils.isAssignable(AggregateRoot.class, o.getClass())) {
                        aggregates.add((AggregateRoot) o);
                    }
                });
        return aggregates;
    }


}
