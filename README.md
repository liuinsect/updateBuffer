updateBuffer
============

异步缓冲更新工具

1. 通过将每次更新的内容缓存再本地，达到阀值后再更新的策略，在减少数据持久化端压力的同时，避免脏写问题。
2. 通过“自适应”阀值的策略，达到更新频率的自动调节， 在满足尽快获取更新结果和减少服务器压力两方面做平衡。

同时，提供灵活的持久化，触发策略，其中PersistentStrategy为持久化策略接口，TriggerStrategy 为触发策略接口

handler同时持有持久化策略和触发策略接口，现在已经提供了 TriggerStrategy 的默认实现：

UpdateNumTrigger：根据更新次数，触发更新策略。
ScheduleTrigger: 根据更新时间，触发更新策略，配置方式同quartz表达式， 例如：*/5 * * * * ?


next step:
1. 融合Profiler记录日志和方法执行时间。
