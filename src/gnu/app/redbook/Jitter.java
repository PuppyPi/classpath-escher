package gnu.app.redbook;


/** 
 * Jitter point arrays for 2, 3, 4, 8, 15, 24 and 66 jitters. Values are
 * floating point in the range -.5 < x < .5, -.5 < y < .5, and have a
 * gaussian distribution around the origin.
 *
 * <p>Use these to do model jittering for scene anti-aliasing and view volume
 * jittering for depth of field effects.
 *
 * <p>Modified from <code>jitter.h</code> in <a href=
 * "http://trant.sgi.com/opengl/examples/redbook/redbook.html">
 * redbook</a> by SGI.
*/
class Jitter {
  static final float [] [] J2 = {
    {0.246490f, 0.249999f},
    {-0.246490f, -0.249999f}
  };


  static final float [] [] J3 = {
    {-0.373411f, -0.250550f},
    {0.256263f, 0.368119f},
    {0.117148f, -0.117570f}
  };


  static final float [] [] J4 = {
    {-0.208147f, 0.353730f},
    {0.203849f, -0.353780f},
    {-0.292626f, -0.149945f},
    {0.296924f, 0.149994f}
  };


  static final float [] [] J8 = {
    {-0.334818f, 0.435331f},
    {0.286438f, -0.393495f},
    {0.459462f, 0.141540f},
    {-0.414498f, -0.192829f},
    {-0.183790f, 0.082102f},
    {-0.079263f, -0.317383f},
    {0.102254f, 0.299133f},
    {0.164216f, -0.054399f}
  };


  static final float [] [] J15 = {
    {0.285561f, 0.188437f},
    {0.360176f, -0.065688f},
    {-0.111751f, 0.275019f},
    {-0.055918f, -0.215197f},
    {-0.080231f, -0.470965f},
    {0.138721f, 0.409168f},
    {0.384120f, 0.458500f},
    {-0.454968f, 0.134088f},
    {0.179271f, -0.331196f},
    {-0.307049f, -0.364927f},
    {0.105354f, -0.010099f},
    {-0.154180f, 0.021794f},
    {-0.370135f, -0.116425f},
    {0.451636f, -0.300013f},
    {-0.370610f, 0.387504f}
  };


  static final float [] [] J24 = {
    {0.030245f, 0.136384f},
    {0.018865f, -0.348867f},
    {-0.350114f, -0.472309f},
    {0.222181f, 0.149524f},
    {-0.393670f, -0.266873f},
    {0.404568f, 0.230436f},
    {0.098381f, 0.465337f},
    {0.462671f, 0.442116f},
    {0.400373f, -0.212720f},
    {-0.409988f, 0.263345f},
    {-0.115878f, -0.001981f},
    {0.348425f, -0.009237f},
    {-0.464016f, 0.066467f},
    {-0.138674f, -0.468006f},
    {0.144932f, -0.022780f},
    {-0.250195f, 0.150161f},
    {-0.181400f, -0.264219f},
    {0.196097f, -0.234139f},
    {-0.311082f, -0.078815f},
    {0.268379f, 0.366778f},
    {-0.040601f, 0.327109f},
    {-0.234392f, 0.354659f},
    {-0.003102f, -0.154402f},
    {0.297997f, -0.417965f}
  };


  static final float [] [] J66 = {
    {0.266377f, -0.218171f},
    {-0.170919f, -0.429368f},
    {0.047356f, -0.387135f},
    {-0.430063f, 0.363413f},
    {-0.221638f, -0.313768f},
    {0.124758f, -0.197109f},
    {-0.400021f, 0.482195f},
    {0.247882f, 0.152010f},
    {-0.286709f, -0.470214f},
    {-0.426790f, 0.004977f},
    {-0.361249f, -0.104549f},
    {-0.040643f, 0.123453f},
    {-0.189296f, 0.438963f},
    {-0.453521f, -0.299889f},
    {0.408216f, -0.457699f},
    {0.328973f, -0.101914f},
    {-0.055540f, -0.477952f},
    {0.194421f, 0.453510f},
    {0.404051f, 0.224974f},
    {0.310136f, 0.419700f},
    {-0.021743f, 0.403898f},
    {-0.466210f, 0.248839f},
    {0.341369f, 0.081490f},
    {0.124156f, -0.016859f},
    {-0.461321f, -0.176661f},
    {0.013210f, 0.234401f},
    {0.174258f, -0.311854f},
    {0.294061f, 0.263364f},
    {-0.114836f, 0.328189f},
    {0.041206f, -0.106205f},
    {0.079227f, 0.345021f},
    {-0.109319f, -0.242380f},
    {0.425005f, -0.332397f},
    {0.009146f, 0.015098f},
    {-0.339084f, -0.355707f},
    {-0.224596f, -0.189548f},
    {0.083475f, 0.117028f},
    {0.295962f, -0.334699f},
    {0.452998f, 0.025397f},
    {0.206511f, -0.104668f},
    {0.447544f, -0.096004f},
    {-0.108006f, -0.002471f},
    {-0.380810f, 0.130036f},
    {-0.242440f, 0.186934f},
    {-0.200363f, 0.070863f},
    {-0.344844f, -0.230814f},
    {0.408660f, 0.345826f},
    {-0.233016f, 0.305203f},
    {0.158475f, -0.430762f},
    {0.486972f, 0.139163f},
    {-0.301610f, 0.009319f},
    {0.282245f, -0.458671f},
    {0.482046f, 0.443890f},
    {-0.121527f, 0.210223f},
    {-0.477606f, -0.424878f},
    {-0.083941f, -0.121440f},
    {-0.345773f, 0.253779f},
    {0.234646f, 0.034549f},
    {0.394102f, -0.210901f},
    {-0.312571f, 0.397656f},
    {0.200906f, 0.333293f},
    {0.018703f, -0.261792f},
    {-0.209349f, -0.065383f},
    {0.076248f, 0.478538f},
    {-0.073036f, -0.355064f},
    {0.145087f, 0.221726f}
  };
}
