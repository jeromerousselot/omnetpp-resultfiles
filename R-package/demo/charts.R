require(omnetpp)

scalarFiles <- file.path(system.file('extdata', package='omnetpp'), 'PureAlohaExperiment-*.sca')
vectorFiles <- c(system.file('extdata', 'OneFifo-0.vec', package='omnetpp'),
                 system.file('extdata', 'TokenRing1-0.vec', package='omnetpp'))

#
# Scatter chart from OMNeT++ samples/resultfiles/PureAlohaExperiment.anf 
#
# TODO iso lines not yet implemented
dataset <- loadDataset(scalarFiles,
             add(type='scalar', select='module(Aloha.server) AND name("channel utilization")'),
             add(type='scalar', select='name(mean)'))
xydata <- createScatterChartDataset(dataset, xModule='.', xName='mean', averageReplications=TRUE)
plotLineChart(xydata, list(X.Axis.Title='Mean packet interarrival time', Y.Axis.Title='Utilization'))


#
# Histogram from OMNeT++ samples/resultfiles/PureAlohaExperiment.anf 
#
dataset <- loadDataset(file.path(system.file('extdata', package='omnetpp'), 'PureAlohaExperiment-*.sca'),
             add('histogram', 'module(Aloha.server) AND name("collision multiplicity") AND run(PureAlohaExperiment-1-*)'))
histograms <- createHistograms(dataset)
plotHistogramChart(histograms, list(X.Axis.Title='number of colliding packets', Y.Axis.Title='number of times occured'))

#
# Plotting vectors.
#
dataset <- loadDataset(vectorFiles, add(select='name("Queue length (packets)")'))
data <- loadVectors(dataset$vectors)
plotLineChart(split(data$vectordata, data$vectordata$vector_key), list(X.Axis.Title='simulation time', Y.Axis.Title='queue length'))

#
# Some bar charts.
#
dataset <- loadDataset(scalarFiles, add('scalar', 'module("Aloha.server") AND file(*PureAlohaExperiment-4*.sca)'))
print(dataset)
d <- createBarChartDataset(dataset, rows=c('measurement'), columns=c('name'))
par(mfrow=c(2,2))
plotBarChart(d, list(Legend.Display='true', Legend.Anchoring='NorthWest'))
plotBarChart(d, list(Bar.Placement='Stacked'))
plotBarChart(d, list(Bar.Placement='Overlapped'))
plotBarChart(d, list(Bar.Placement='Infront'))