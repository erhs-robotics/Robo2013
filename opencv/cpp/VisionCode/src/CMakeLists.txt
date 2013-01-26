include_directories(${CMAKE_CURRENT_BINARY_DIR})
set(GUI_SRCS gui.cpp)

add_executable(visionGUI ${GUI_SRCS})
target_link_libraries(visionGUI ${OpenCV_LIBS} vision)
