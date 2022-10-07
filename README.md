# System requirements and installation

1, Windows 7 sp1 or newer, x64.

2, NVidia CUDA enabled GPU with compute capability no less than 3.5.

**Note: please upgrade your GPU driver to the newest (https://www.geforce.com/drivers , Compatible Driver Versions >= 411.31) or the plug-in can't work successfully.**

3, ImageJ/Fiji, Micro-Manager 2.0 (beta or gamma). **Note: please set at least 6 GB memory buffer for ImageJ, Micro-Manager and ImageJ of Micro-Manager.** (The 64 bits ImageJ with Java 1.8 works well and can be downloaded at https://imagej.nih.gov/ij/download.html)

4, Download and install Microsoft Visual C++ 2015 Redistributable Update 3 (x64) (https://www.microsoft.com/en-us/download/details.aspx?id=53587). You may need to uninstall Visual C++ 2017 Redistributable first.

6, Download and install Matlab 2020b

5, Install plugin

6, Note: NanoStitcher only supports English file name / path.

# Output file format

The NanoStitcher generates a general txt file and a binary txt file in the result save path.

The offset value of each localization is stored in the general txt，witch has "minTree" in the middle of the file name.

There are 12 float parameters saved for each localizations in the binary txt file, please see the user's guide of NanoStitcher.

# How to install and use
See *user's guide of NanoStitcher.pdf* for more details

# Samples

In the “Samples” folder, the “2x2” folder contains the localization binary files and the “NanoStitcherResult” folder contains the final stitching and rendering result.

| Stitching result’s parameters                   |      |
| ----------------------------------------------- | ---- |
| Number of localization list in the x-direction. | 2    |
| Number of localization list in the y-direction. | 2    |
| Extent of overlap between datasets.             | 50   |

 

| Rendering result’s parameters   |       |
| ------------------------------- | ----- |
| Raw image pixel size(nm):       | 107.0 |
| Rendering image pixel size(nm): | 10.0  |
| Rendering SNR thrshold:         | 5.0   |
