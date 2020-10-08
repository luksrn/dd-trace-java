import datadog.trace.agent.test.AgentTestRunner
import datadog.trace.core.DDSpan
import spock.lang.Requires
import spock.lang.Shared

import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool

@Requires({!System.getProperty("java.version").startsWith("1.7")})
class RecursiveThreadPoolPropagationTest extends AgentTestRunner {

  @Shared
  def recursiveSubmission = { executor, depth -> executor.submit(new RecursiveThreadPoolSubmission(executor, depth, 0)) }
  @Shared
  def recursiveExecution = { executor, depth -> executor.submit(new RecursiveThreadPoolExecution(executor, depth, 0)) }
  @Shared
  def mixedSubmissionAndExecution = { executor, depth -> executor.submit(new RecursiveThreadPoolMixedSubmissionAndExecution(executor, depth, 0)) }

  def "propagate context in #executor with #parallelism threads #depth times"() {
    when:
    test(executor, depth)
    then:
    assertTrace(depth)

    cleanup:
    executor.shutdownNow()

    where:
    depth | parallelism                             | test                        | executor
    1     | 2                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    1     | 3                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    1     | 4                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    2     | 1                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    2     | 2                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    2     | 3                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    2     | 4                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    3     | 1                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    3     | 2                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    3     | 3                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    3     | 4                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    4     | 1                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    4     | 2                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    4     | 3                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    4     | 4                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    5     | 1                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    5     | 2                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    5     | 3                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    5     | 4                                       | recursiveSubmission         | Executors.newFixedThreadPool(parallelism)
    1     | 2                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    1     | 3                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    1     | 4                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    2     | 1                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    2     | 2                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    2     | 3                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    2     | 4                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    3     | 1                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    3     | 2                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    3     | 3                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    3     | 4                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    4     | 1                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    4     | 2                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    4     | 3                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    4     | 4                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    5     | 1                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    5     | 2                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    5     | 3                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    5     | 4                                       | recursiveExecution          | Executors.newFixedThreadPool(parallelism)
    1     | 2                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    1     | 3                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    1     | 4                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    2     | 1                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    2     | 2                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    2     | 3                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    2     | 4                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    3     | 1                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    3     | 2                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    3     | 3                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    3     | 4                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    4     | 1                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    4     | 2                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    4     | 3                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    4     | 4                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    5     | 1                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    5     | 2                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    5     | 3                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)
    5     | 4                                       | mixedSubmissionAndExecution | Executors.newFixedThreadPool(parallelism)

    1     | 2                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    1     | 3                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    1     | 4                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    2     | 1                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    2     | 2                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    2     | 3                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    2     | 4                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    3     | 1                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    3     | 2                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    3     | 3                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    3     | 4                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    4     | 1                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    4     | 2                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    4     | 3                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    4     | 4                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    5     | 1                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    5     | 2                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    5     | 3                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    5     | 4                                       | recursiveSubmission         | Executors.newScheduledThreadPool(parallelism)
    1     | 2                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    1     | 3                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    1     | 4                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    2     | 1                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    2     | 2                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    2     | 3                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    2     | 4                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    3     | 1                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    3     | 2                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    3     | 3                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    3     | 4                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    4     | 1                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    4     | 2                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    4     | 3                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    4     | 4                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    5     | 1                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    5     | 2                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    5     | 3                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    5     | 4                                       | recursiveExecution          | Executors.newScheduledThreadPool(parallelism)
    1     | 2                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    1     | 3                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    1     | 4                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    2     | 1                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    2     | 2                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    2     | 3                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    2     | 4                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    3     | 1                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    3     | 2                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    3     | 3                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    3     | 4                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    4     | 1                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    4     | 2                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    4     | 3                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    4     | 4                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    5     | 1                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    5     | 2                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    5     | 3                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)
    5     | 4                                       | mixedSubmissionAndExecution | Executors.newScheduledThreadPool(parallelism)

    1     | 2                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    1     | 3                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    1     | 4                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    2     | 1                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    2     | 2                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    2     | 3                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    2     | 4                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    3     | 1                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    3     | 2                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    3     | 3                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    3     | 4                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    4     | 1                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    4     | 2                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    4     | 3                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    4     | 4                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    5     | 1                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    5     | 2                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    5     | 3                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    5     | 4                                       | recursiveSubmission         | new ForkJoinPool(parallelism)
    1     | 2                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    1     | 3                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    1     | 4                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    2     | 1                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    2     | 2                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    2     | 3                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    2     | 4                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    3     | 1                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    3     | 2                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    3     | 3                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    3     | 4                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    4     | 1                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    4     | 2                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    4     | 3                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    4     | 4                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    5     | 1                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    5     | 2                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    5     | 3                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    5     | 4                                       | recursiveExecution          | new ForkJoinPool(parallelism)
    1     | 2                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    1     | 3                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    1     | 4                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    2     | 1                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    2     | 2                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    2     | 3                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    2     | 4                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    3     | 1                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    3     | 2                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    3     | 3                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    3     | 4                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    4     | 1                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    4     | 2                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    4     | 3                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    4     | 4                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    5     | 1                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    5     | 2                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    5     | 3                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)
    5     | 4                                       | mixedSubmissionAndExecution | new ForkJoinPool(parallelism)

    1     | ForkJoinPool.getCommonPoolParallelism() | recursiveSubmission         | ForkJoinPool.commonPool()
    2     | ForkJoinPool.getCommonPoolParallelism() | recursiveSubmission         | ForkJoinPool.commonPool()
    3     | ForkJoinPool.getCommonPoolParallelism() | recursiveSubmission         | ForkJoinPool.commonPool()
    4     | ForkJoinPool.getCommonPoolParallelism() | recursiveSubmission         | ForkJoinPool.commonPool()
    5     | ForkJoinPool.getCommonPoolParallelism() | recursiveSubmission         | ForkJoinPool.commonPool()
    1     | ForkJoinPool.getCommonPoolParallelism() | recursiveExecution          | ForkJoinPool.commonPool()
    2     | ForkJoinPool.getCommonPoolParallelism() | recursiveExecution          | ForkJoinPool.commonPool()
    3     | ForkJoinPool.getCommonPoolParallelism() | recursiveExecution          | ForkJoinPool.commonPool()
    4     | ForkJoinPool.getCommonPoolParallelism() | recursiveExecution          | ForkJoinPool.commonPool()
    5     | ForkJoinPool.getCommonPoolParallelism() | recursiveExecution          | ForkJoinPool.commonPool()
    1     | ForkJoinPool.getCommonPoolParallelism() | mixedSubmissionAndExecution | ForkJoinPool.commonPool()
    2     | ForkJoinPool.getCommonPoolParallelism() | mixedSubmissionAndExecution | ForkJoinPool.commonPool()
    3     | ForkJoinPool.getCommonPoolParallelism() | mixedSubmissionAndExecution | ForkJoinPool.commonPool()
    4     | ForkJoinPool.getCommonPoolParallelism() | mixedSubmissionAndExecution | ForkJoinPool.commonPool()
    5     | ForkJoinPool.getCommonPoolParallelism() | mixedSubmissionAndExecution | ForkJoinPool.commonPool()
  }

  private static void assertTrace(int depth) {
    TEST_WRITER.waitForTraces(1)
    TEST_WRITER.size() == 1
    int i = 0
    int orphanCount = 0
    List<DDSpan> trace = TEST_WRITER.get(0)
    assert trace.size() == depth
    sortByDepth(trace)
    for (DDSpan span : trace) {
      orphanCount += span.isRootSpan() ? 1 : 0
      assert String.valueOf(i++) == span.getOperationName()
    }
    assert orphanCount == 1
  }

  private static void sortByDepth(List<DDSpan> trace) {
    Collections.sort(trace, new Comparator<DDSpan>() {
      @Override
      int compare(DDSpan l, DDSpan r) {
        return Integer.parseInt(l.getOperationName().toString()) - Integer.parseInt(r.getOperationName().toString())
      }
    })
  }
}
